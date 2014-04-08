package com.host.node;

import org.codehaus.jackson.map.ObjectMapper;

import com.host.node.model.UserCommandDTO;
import com.host.node.request.MainGetRequest;

public class ScanCommandThread extends Thread {
	
	private int refreshRate = 3000;
	
	private boolean isContinue = true;
	public static ObjectMapper objectMapper = new ObjectMapper();
	
	public ScanCommandThread() {
		super();
	}
	
	@Override
	public void run() {
		
		while(isContinue()) {
			try {
				// 1. Get Last Command mac address
				MainGetRequest cmdRequest = new MainGetRequest();
				cmdRequest.setUrl("services/command/userCommandService/lastcommand/" + MainController.macAddress);
				String cmdResponse = cmdRequest.execute();
				 
				System.out.println("cmdResponse: " + cmdResponse);
				
				if (cmdResponse != null && !cmdResponse.isEmpty()) {
					// Create Process Command Tread
					UserCommandDTO jsonObject = objectMapper.readValue(cmdResponse, UserCommandDTO.class);
					
					if (jsonObject.getId() != null) {
						ProcessCommandThread thread = new ProcessCommandThread(jsonObject);
						thread.start();
					}
					
					System.out.println("cmdStr: " + jsonObject.getCommandStr());
				}
			
			
				Thread.sleep(getRefreshRate());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public int getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(int refreshRate) {
		this.refreshRate = refreshRate;
	}

	public boolean isContinue() {
		return isContinue;
	}

	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}
}
