package com.host.node;

import com.host.node.request.MainGetRequest;

public class ScanCommandThread extends Thread {
	
	private int refreshRate = 3000;
	
	private boolean isContinue = true;
	
	public ScanCommandThread() {
		super();
	}
	
	@Override
	public void run() {
		
		while(isContinue()) {
			// 1. Get Last Command mac address
			MainGetRequest cmdRequest = new MainGetRequest();
			cmdRequest.setUrl("services/command/userCommandService/lastcommand/" + MainController.macAddress);
			String cmdResponse = cmdRequest.execute();
			 
			System.out.println("cmdResponse: " + cmdResponse);
						
			if (cmdResponse != null && cmdResponse.isEmpty()) {
				// Create Process Command Tread
			}
			
			try {
				Thread.sleep(getRefreshRate());
			} catch (InterruptedException e) {
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
