
package com.team.mighty.tasks.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team.mighty.dao.SettingDao;
import com.team.mighty.logger.MightyLogger;

@Service
public class UtilitySupport implements Runnable {

	private static final MightyLogger logger = MightyLogger.getLogger(UtilitySupport.class);
	public static Date currentDate = new Date();
	
	@Autowired
	private SettingDao settingDao;

	public void run() {
		logger.debug("WORKING THE UTLITY FIRSTv \n currrenttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
		DateFormat formatter;
			try {
					formatter = new SimpleDateFormat("yyyy-MM-dd");
					String curDate = formatter.format(new Date());
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DATE, -1);
					String yesterDay = formatter.format(cal.getTime());
					
					if (isUtilityRanOn(curDate) == false) {
								
						  try{
											
									/*new EmployeeSubscriptionService()
											.sendEmailToEmployeesWhoAreAboutTheUnsubscription(
									*/
							}catch(Exception e){
								logger.error("errro in Mighty Users GEn Reports", e);
							}
							
					}
			
				}catch (Exception e) {
					logger.error("Error in utility run", e);
				}finally{
				}
	}
	
	
	Date getUtiltiyLastUpdatedDate(){
		Date date = settingDao.getUtilityLastUpdatedDate().get(0).getUtilityLastUpdatedDate();
			return date;
	}
	
	public void vehiclePositionTableChange() {
	}

	boolean isUtilityRanOn(String currentDate) {
		logger.debug("IN RAN ON",currentDate);
		Date lastUpdated = getUtiltiyLastUpdatedDate();

		logger.debug(lastUpdated, "return");
		if (lastUpdated == null	|| lastUpdated.toString().compareTo(currentDate.toString()) < 0) {
			return false;
		} else {
			return true;
		}

	}

	public void cleanUp() {
		// System.out.println("ENDING___");
	}

}
