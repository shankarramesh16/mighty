package com.team.mighty.notification;



public class SendMailFactory {
 
	public static String mailFlag = "mighty";
 

	private SendMailFactory() {

	}

	public static SendMail getMailInstance() {
		if (mailFlag.equalsIgnoreCase("mighty")) {
			return (SendMail) new SendGmail();
		}else{
			return (SendMail) new SendGmail();
		}
	}	
}
