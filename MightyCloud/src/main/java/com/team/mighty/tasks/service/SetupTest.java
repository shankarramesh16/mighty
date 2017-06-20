package com.team.mighty.tasks.service;

import org.springframework.stereotype.Service;

import com.team.mighty.logger.MightyLogger;

@Service
public class SetupTest {
	private static final MightyLogger logger = MightyLogger.getLogger(SetupTest.class);
	
	/*@PostConstruct*/
	public void init(){
		logger.debug("Utility initialization");
		Scheduler.getInstance().startTasks();
	}
}
