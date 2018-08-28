package com.team.mighty.notification;



public class SendMailFactory {
 
	public static String mailFlag = "uswm";
 

	private SendMailFactory() {

	}

	public static SendMail getMailInstance() {
		if (mailFlag.equalsIgnoreCase("uswm")) {
			return (SendMail) new SendGmail();
		}else{
			return (SendMail) new SendGmail();
		}
	}	
}
