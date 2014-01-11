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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Component;

import com.jinhs.safeguard.common.TrackingDataBO;

@Component
public class EmailHandler {
	@Autowired
    private MailSender mailSender;
	
	@Autowired
	private DataHandler dataHandler;
	
	public void sendEmailGAE(String userId) throws UnsupportedEncodingException{
		List<TrackingDataBO> list = dataHandler.getData(userId);
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("jin231489@gmail.com", "GlassGuard"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress("huangshuaijin@gmail.com", "Mr. User"));
            msg.setSubject("Glassguard Alert! Your friend need your help!");
            msg.setText(buildMessage(list, userId));
            Transport.send(msg);

        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        }
	}

	private String buildMessage(List<TrackingDataBO> list, String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("Google Glass had detected an abnormal shock and your friend maybe in danger.\n");
		sb.append("Call (412) 231-3214 To ensure your friend is alright");
		sb.append("Here is the tracking information of your friend which shows his/her last known locations and surrounding data\n");
		sb.append("Complete tracking page of your friend:"+"http://jinhsglassguard.appspot.com/view/trackinginfo"+"?userId="+userId);
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
