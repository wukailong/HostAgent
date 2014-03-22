package com.host.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestClass {
	
	public static void main(String[] args) {
		try {
			Process pro = Runtime.getRuntime().exec("cmd /c tasklist"); 
		    BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream())); 
		    String msg = null; 
		    while ((msg = br.readLine()) != null) { 
		    	System.out.println(msg); 
		    }
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
   
//		try {
//
//			   // 登网站
//			   Process p = Runtime.getRuntime().exec("cmd.exe  /c  start  http://www.hao123.net/");
//
//			   // 使用用Ping命令
////			   Process p = Runtime.getRuntime().exec("cmd.exe  /c  start  ping 10.5.2.19");
//			   
//			   
//			   
//			   InputStream is = p.getInputStream();
//			   int data;
//			   while((data = is.read()) != -1) {
//				   System.out.print((char)data);
//			   }
//
//			  } catch (Exception e) {
//				  e.printStackTrace();
//			  }
	}

}
