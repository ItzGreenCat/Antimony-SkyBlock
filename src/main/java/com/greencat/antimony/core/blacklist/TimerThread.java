package com.greencat.antimony.core.blacklist;

public class TimerThread extends Thread {

	private TimerUtil timer = new TimerUtil();
	
	@Override
	public void run() {
		while(true) {
			try {
				if(this.timer.hasReached(60 * 1000)) {
					BlackListManager.load();
					this.timer.reset();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
