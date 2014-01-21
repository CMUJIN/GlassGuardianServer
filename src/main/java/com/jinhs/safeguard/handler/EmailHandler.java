package com.jinhs.safeguard.handler;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Component;

import com.jinhs.safeguard.dao.DBTransService;

@Component
public class EmailHandler {
	@Autowired
    private MailSender mailSender;
	
	@Autowired
	private DataHandler dataHandler;
	
	@Autowired
	private DBTransService transService;
	
	public void sendEmailGAE(String userId, String key) throws UnsupportedEncodingException{
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        List<String> emailList = transService.getAlertEmail(userId);
        for(String toEmail: emailList)
        	sendEmail(key, toEmail, session);
	}

	private void sendEmail(String key, String toEmail, Session session) throws UnsupportedEncodingException {
		try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("jin231489@gmail.com", "Glass Guardian"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(toEmail, "Mr. User"));
            msg.setSubject("Glassguard Alert! Your friend need your help!");
            msg.setText(buildMessage(key));
            Transport.send(msg);

        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        }
	}

	private String buildMessage(String key) {
		StringBuffer sb = new StringBuffer();
		sb.append("Google Glass had detected an abnormal shock and your friend maybe in danger.\n");
		sb.append("\n");
		sb.append("Click below link to see his/her latest environment information\n");
		sb.append("http://jinhsglassguard.appspot.com/view/trackinginfo"+"?userId="+key);
		sb.append("\n");
		
		return sb.toString();
	}

	private String convertTimeZone(Date date) {
		Calendar c= Calendar.getInstance(TimeZone.getTimeZone("UTC-8"));
		c.setTime(date);
		c.add(Calendar.HOUR, -8);
		return new Date(c.getTimeInMillis()+TimeZone.getTimeZone("UTC-8").getOffset(c.getTimeInMillis())).toString();
		
	}
}
