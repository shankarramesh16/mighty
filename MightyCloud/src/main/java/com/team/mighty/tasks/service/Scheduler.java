package com.team.mighty.tasks.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team.mighty.dao.SettingDao;
import com.team.mighty.domain.Setting;
import com.team.mighty.logger.MightyLogger;

@Service
public class Scheduler {

	private static final MightyLogger logger = MightyLogger.getLogger(Scheduler.class);
	private static Scheduler i_scheduler;
	private Timer timer;
	
	@Autowired
	private UtilityServices utilityServices;
	
	@Autowired
	private SettingDao settingDao;
	
	

	private Scheduler() {
	}

	public static Scheduler getInstance() {
		if (i_scheduler == null) {
			i_scheduler = new Scheduler();
		}
		return i_scheduler;
	}

	public boolean startTasks() {
		try {

			// updatedatetime.set(Calendar.HOUR, 16);
			// updatedatetime.set(Calendar.MINUTE, 38);

			logger.debug("//////////////////////////////////StartTasks/////////////////////////////////////////");
			timer = new Timer();
			Calendar updatedatetime = Calendar.getInstance();
			logger.debug("IN GetUtilityrunningtime");
			//int setting=settingDao.getUtilityLastUpdatedDate().size();
			logger.debug("GetUtilityrunningtime",settingDao.getUtilityLastUpdatedDate());
			String time[] = settingDao.getUtilityLastUpdatedDate().get(0).getUtilityrunningtime().split(":");
			logger.debug("GetUtilityrunningtime",time);
			if (time != null) {
				try {
					updatedatetime.setTime(new Date());
					updatedatetime
							.set(Calendar.HOUR, Integer.parseInt(time[0]));
					updatedatetime.set(Calendar.MINUTE,
							Integer.parseInt(time[1]));
					updatedatetime.set(Calendar.SECOND,
							Integer.parseInt(time[2]));
					if (time[3].equals("AM")) {
						updatedatetime.set(Calendar.AM_PM, Calendar.AM);
					} else {
						updatedatetime.set(Calendar.AM_PM, Calendar.PM);
					}
				} catch (Exception e) {
					time = null;
				}

			}
			if (time == null) {

				updatedatetime.setTime(new Date());
				updatedatetime.set(Calendar.HOUR, 12);
				updatedatetime.set(Calendar.MINUTE, 02);
				updatedatetime.set(Calendar.SECOND, 00);
				updatedatetime.set(Calendar.AM_PM, Calendar.AM);
			}
			Date dt = updatedatetime.getTime();
			//utilityServices = new UtilityServices();
			timer.scheduleAtFixedRate(utilityServices , dt,
					1000 * 60 * 60 * 24);
		} catch (Exception e) {
			logger.debug("Error in Schedular", e);
			return false;
		}
		return true;
	}

	public boolean endTasks() {
		try {
			utilityServices.cancel();
		}catch(Exception e) {}
		try {
			timer.cancel();
		}catch(Exception e) {}
		return true;
	}
}
