package com.team.mighty.tasks.service;

import java.util.TimerTask;

import org.springframework.stereotype.Service;

import com.team.mighty.logger.MightyLogger;

@Service
public class UtilityServices extends TimerTask {
	private static final MightyLogger logger = MightyLogger.getLogger(UtilityServices.class);
	Thread ut = null;
	public void run() {
		
		try{
			logger.debug("Utility Services Running");
			 ut = new Thread(new UtilitySupport());
			ut.setPriority(Thread.MAX_PRIORITY);
			ut.start();
			//System.out.println("no utility run");
			ut.join();
		}catch(Exception e) {
			logger.error("Exception in UtilityServices : ", e);

		}		
	}
	
	
	

}
