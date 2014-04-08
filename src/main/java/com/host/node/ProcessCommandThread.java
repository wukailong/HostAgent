package com.host.node;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import org.codehaus.jackson.map.ObjectMapper;

import com.host.node.model.UserCommandDTO;
import com.host.node.request.MainPostRequest;


public class ProcessCommandThread extends Thread {
	
	private UserCommandDTO command;
	public static ObjectMapper objectMapper = new ObjectMapper();
	
	public ProcessCommandThread (UserCommandDTO command) {
		this.command = command;
	}
	
	@Override
	public void run() {
//		String cmd = "ping www.baidu.com";     
		String cmd = "ipconfig";
		if (command.getCommandStr() != null && !command.getCommandStr().isEmpty()) {
			cmd = command.getCommandStr();
		}
		
		StringBuffer resultBuffer = new StringBuffer();
		boolean isSucess = true;
		
		Runtime run = Runtime.getRuntime();
		try {
			  Process p = run.exec(cmd);// 启动另一个进程来执行命令     
			  BufferedInputStream in = new BufferedInputStream(p.getInputStream());     
		      BufferedReader inBr = new BufferedReader(new InputStreamReader(in, System.getProperty("sun.jnu.encoding")));     
		      String lineStr;     
		      while ((lineStr = inBr.readLine()) != null) {    
		          //获得命令执行后在控制台的输出信息
		    	  lineStr = lineStr.replaceAll("\\n\\r", "<br/>").replaceAll("\\n", "<br/>").replaceAll("\\r", "");
//		    	  lineStr = new String(lineStr.getBytes(System.getProperty("sun.jnu.encoding")), "UTF-8");
		    	  
		          System.out.println("lineStr***: " + lineStr);// 打印输出信息
		          resultBuffer.append(lineStr);
		      }
		      //检查命令是否执行失败。     
		      if (p.waitFor() != 0) {     
		          if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束   
		        	  isSucess = false;
		              System.err.println("命令执行失败!");     
		      }     
		      inBr.close();     
		      in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		command.setResultStr(resultBuffer.toString());
		if (isSucess) {
			command.setStatus("Sucess");
		} else {
			command.setStatus("Failed");
		}
			
		
		  MainPostRequest request = new MainPostRequest();
		  request.setUrl("services/command/userCommandService/create");
		  
		  ByteArrayOutputStream bos = new ByteArrayOutputStream();
		  try {
			objectMapper.writeValue(bos, command);
		  } catch (Exception e) {
				e.printStackTrace();
		  }
		  String newCommandJson = bos.toString();
		  
		  System.out.println("newCommandJson: " + newCommandJson);
		  
		  request.setPostDataJsonStr(newCommandJson);
//		  request.setPostDataJsonStr(fromObject(status).toString());
		  String resultDataJsonStr = request.execute();
		  
		  System.out.println("updateCommandJson Return: " + resultDataJsonStr);
		
		
	}
	
}
