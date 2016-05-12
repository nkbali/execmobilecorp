package com.execmobile.helpers;

import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

public class Mailer {
	
	public void sendMail(byte[] data)
	{

		try {
			Session session = getMailSession();
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("alert@corp.execmobile.co.za"));
			InternetAddress[] recipients = new InternetAddress[] {new InternetAddress("craig.lowe@execmobile.co.za"),
                    new InternetAddress("accounts@execmobile.co.za")};
			message.setRecipients(Message.RecipientType.TO, recipients);
			message.setSubject("Monthly exceptional usage report");
			
			ByteArrayDataSource source = new ByteArrayDataSource(data,"application/vnd.ms-excel");
			
			MimeBodyPart messageBody = new MimeBodyPart(); 
			messageBody.setText("Dear Craig,"
						+ "\n\n PFA the exceptional usage report for the month");

		     MimeBodyPart messageAttachment = new MimeBodyPart();
		     messageAttachment.setDataHandler(new DataHandler(source));   
		     messageAttachment.setFileName("Exceptional Usage Report -" + LocalDate.now(DateTimeZone.UTC).toString() + ".xlsx");    

		     Multipart multiPartMessage = new MimeMultipart();   
		     multiPartMessage.addBodyPart(messageBody);   
		     multiPartMessage.addBodyPart(messageAttachment);   
		     message.setContent(multiPartMessage);   
		     message.saveChanges();  
			
			Transport.send(message);

			

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void sendPasswordResetMail(String company)
	{

		try {
			Session session = getMailSession();
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("alert@corp.execmobile.co.za"));
			InternetAddress[] recipients = new InternetAddress[] {new InternetAddress("craig.lowe@execmobile.co.za"),
                    new InternetAddress("accounts@execmobile.co.za")};
			message.setRecipients(Message.RecipientType.TO, recipients);
			message.setSubject("Password Reset Requested");
			
			
			MimeBodyPart messageBody = new MimeBodyPart(); 
			messageBody.setText("Dear Craig,"
					+ "\n\n The user for " + company + " has request a password reset.");

		     
		     Multipart multiPartMessage = new MimeMultipart();   
		     multiPartMessage.addBodyPart(messageBody);   
		     message.setContent(multiPartMessage);   
		     message.saveChanges();  
			
			Transport.send(message);

			

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void sendMailToAppUsers(List<String> addresses, String subject, String body){
		try {
			Session session = getMailSession();
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("alert@corp.execmobile.co.za"));
			InternetAddress[] recipients = new InternetAddress[addresses.size()];
			int i= 0;
			for(String address : addresses){
				InternetAddress recipient = new InternetAddress(address);
				recipients[i] = recipient;
				i++;
			}
			
			message.setRecipients(Message.RecipientType.BCC, recipients);
			message.setSubject(subject);
			
			
			MimeBodyPart messageBody = new MimeBodyPart(); 
			messageBody.setText(body);

		     
		     Multipart multiPartMessage = new MimeMultipart();   
		     multiPartMessage.addBodyPart(messageBody);   
		     message.setContent(multiPartMessage);   
		     message.saveChanges();  
			
			Transport.send(message);

			

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private Session getMailSession()
	{
		final String username = "alert@corp.execmobile.co.za";
		final String password = "ahmMontell0";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.corp.execmobile.co.za");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		
		return session;
	}
}
