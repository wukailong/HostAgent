package com.host.node;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Timer;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

import com.host.node.model.HostStatusInfo;

public class MainController {
	
	static class RefreshHostInfoTask extends java.util.TimerTask {
		
		@Override
		public void run() {
			System.out.println("------------------------------------------");
			
			Sigar sigar = new Sigar();
			HostStatusInfo status = new HostStatusInfo();
			status.setId(1l);

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
				  
				  
			   
			  } catch (Exception e) { 
				  System.out.println(e.getMessage());
				  e.printStackTrace();
			  } finally {
			  	sigar.close();
			  }
			  
			  
			HttpClient client2 = new DefaultHttpClient();		
			HttpPost httpPost = new HttpPost("http://127.0.0.1:8090/MonitorServer-1.0/services/hostInfo/hostStatusInfoService/create");

			try {
				
				StringEntity inputEntity = new StringEntity(JSONObject.fromObject(status).toString());
				inputEntity.setContentType("application/json");
				httpPost.setEntity(inputEntity);
				
				System.out.println("inputEntity: " + JSONObject.fromObject(status).toString());
				
				HttpResponse response = client2.execute(httpPost);
				String json = EntityUtils.toString(response.getEntity(), "utf-8");
				
				System.out.println("JSON: " + json);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();     
			}
		}
		
	}

	public static void main(String[] args) {
		
		Timer timer = new Timer();
		timer.schedule(new RefreshHostInfoTask(), 1000, 5000);//在1秒后执行此任务,每次间隔2秒,如果传递一个Data参数,就可以在某个固定的时间执行这个任务.
		
		while(true){//这个是用来停止此任务的,否则就一直循环执行此任务了
			try {
				int ch = System.in.read();
				if(ch-'c'==0){
					timer.cancel();//使用这个方法退出任务
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
