package com.host.node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Timer;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.Sigar;

import com.host.node.model.HostStatusInfoDTO;
import com.host.node.model.UserCommandDTO;
import com.host.node.request.MainGetRequest;
import com.host.node.request.MainPostRequest;

public class MainController {
	
	public static String serverUrl = "http://127.0.0.1:8090/MonitorServer-1.0/";
	public static String macAddress = "";
	public static ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		try {
			Sigar sigar = new Sigar();
			NetInterfaceConfig nic = sigar.getNetInterfaceConfig();	// get Default Mac Address
			macAddress = nic.getHwaddr();		
			
			System.out.println("macAddress: " + macAddress);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	static class RefreshHostInfoTask extends java.util.TimerTask {
		
		@Override
		public void run() {
			System.out.println("FileEncoding: " + System.getProperty("file.encoding")); 
			System.out.println("SunJnuEncoding: " + System.getProperty("sun.jnu.encoding"));
//			System.getProperties().list(System.out);
			
			System.out.println("------------------------------------------");
			
			
			
			Sigar sigar = new Sigar();
			HostStatusInfoDTO status = new HostStatusInfoDTO();
			
//			UserCommandDTO dto = new UserCommandDTO();
//			dto.setId(1l);			

			  try { 

				  System.out.println("CPUCount: " + sigar.getCpuInfoList().length); 
				  
				  CpuPerc firstCPU = sigar.getCpuPercList()[0];
				  
				  status.setCpuCount(sigar.getCpuInfoList().length);
				  status.setCpuTotalUsed(firstCPU.getCombined());		
				  
				  Mem mem = sigar.getMem();
				  mem = sigar.getMem(); 
				  
				  status.setTotalMem(mem.getTotal());
				  status.setFreeMem(mem.getFree());

				  // 内存总量
				  System.out.println("Total = " + mem.getTotal() / 1024L + "K av"); 
				  // 当前内存使用量
				  System.out.println("Used = " + mem.getUsed() / 1024L + "K used"); 
				  // 当前内存剩余量
				  System.out.println("Free = " + mem.getFree() / 1024L + "K free");
				  
				  String hostname = InetAddress.getLocalHost().getHostName();
				  hostname = sigar.getNetInfo().getHostName(); 
				  System.out.println("hostname: " + hostname);
				  
				  status.setHostname(hostname);
				  status.setMacAddress(macAddress);
			  
				  
//				  dto.setHostMacAddress(macAddress);
//				  dto.setCommandStr("cmd /c tasklist");
////				  dto.setCreationDate(new Date());
//				  dto.setStatus("Created");

			   
			  } catch (Exception e) { 
				  System.out.println(e.getMessage());
				  e.printStackTrace();
			  } finally {
			  	sigar.close();
			  }
			  
			  // 1. Save New Status
			  MainPostRequest request = new MainPostRequest();
			  request.setUrl("services/hostInfo/hostStatusInfoService/create");
			  
			  ByteArrayOutputStream bos = new ByteArrayOutputStream();
			  try {
				objectMapper.writeValue(bos, status);
			  } catch (Exception e) {
					e.printStackTrace();
			  }
			  String statusJson = bos.toString();
			  
			  System.out.println("SendJson: " + statusJson);
			  
			  request.setPostDataJsonStr(statusJson);
//			  request.setPostDataJsonStr(fromObject(status).toString());
			  String resultDataJsonStr = request.execute();
			  
			  System.out.println("JSON: " + resultDataJsonStr);
			  
//			  // 2. Save New Command
//			  MainPostRequest cmdRequest = new MainPostRequest();
//			  cmdRequest.setUrl("services/command/userCommandService/create");
//			  cmdRequest.setPostDataJsonStr(JSONObject.fromObject(dto).toString());
//			  String cmdResponse = cmdRequest.execute();
//			  
//			  System.out.println("cmdResponse: " + cmdResponse);
			  
//			  // 3. Get Command By Id
//			  MainGetRequest cmdRequest = new MainGetRequest();
//			  cmdRequest.setUrl("services/command/userCommandService/getcommand/1");
//			  String cmdResponse = cmdRequest.execute();
//			  
//			  System.out.println("cmdResponse: " + cmdResponse);
			  
//			  // 3. Get Last Command mac address
//			  MainGetRequest cmdRequest = new MainGetRequest();
//			  cmdRequest.setUrl("services/command/userCommandService/lastcommand/" + macAddress);
//			  String cmdResponse = cmdRequest.execute();
//			  
//			  System.out.println("cmdResponse: " + cmdResponse);
			  
			  
//			// 3. Get All Command mac address
//			  MainGetRequest cmdRequest = new MainGetRequest();
//			  cmdRequest.setUrl("services/command/userCommandService/allcommand/" + macAddress);
//			  String cmdResponse = cmdRequest.execute();
//			  
//			  System.out.println("cmdResponse: " + cmdResponse);
			  
			  
//			HttpClient client2 = new DefaultHttpClient();		
//			HttpPost httpPost = new HttpPost("services/hostInfo/hostStatusInfoService/create");
//
//			try {
//				
//				StringEntity inputEntity = new StringEntity(JSONObject.fromObject(status).toString());
//				inputEntity.setContentType("application/json");
//				httpPost.setEntity(inputEntity);
//				
//				System.out.println("inputEntity: " + JSONObject.fromObject(status).toString());
//				
//				HttpResponse response = client2.execute(httpPost);
//				String json = EntityUtils.toString(response.getEntity(), "utf-8");
//				
////				JSONObject obj = new JSONObject(json);
////				
////				JSONObject.toBean(jsonObject, beanClass);
//				
//				System.out.println("JSON: " + json);
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//				e.printStackTrace();     
//			}
		}
		
	}

	public static void main(String[] args) {
		
		// 1. Refresh Host Status		
		Timer timer = new Timer();
		timer.schedule(new RefreshHostInfoTask(), 1000, 5000);//在1秒后执行此任务,每次间隔5秒,如果传递一个Data参数,就可以在某个固定的时间执行这个任务.
		
		// TODO 2. Get Last Unfinished Command  (Scan)
		ScanCommandThread scanThread = new ScanCommandThread();
		scanThread.start();
				
		while(true){//这个是用来停止此任务的,否则就一直循环执行此任务了
			try {
				int ch = System.in.read();
				if(ch-'c'==0){
					timer.cancel();//使用这个方法退出任务
					scanThread.setContinue(false);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	

}
