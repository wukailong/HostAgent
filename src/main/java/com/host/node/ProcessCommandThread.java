package com.host.node;


public class ProcessCommandThread extends Thread {
	
	private boolean isContinue = true;

	public boolean isContinue() {
		return isContinue;
	}

	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}
	
	@Override
	public void run() {
		
		while(isContinue) {
			
		}
		
	}
	
}
