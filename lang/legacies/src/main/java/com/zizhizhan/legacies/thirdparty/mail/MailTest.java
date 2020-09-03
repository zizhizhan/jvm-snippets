package com.zizhizhan.legacies.thirdparty.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailTest {

	public static void main(String[] args) throws MessagingException {
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.sina.com");
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("mytemp007", "feiyang");
			}
		});

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("mytemp007@sina.com"));
		msg.setSubject("Test Email");
		msg.setRecipients(RecipientType.TO, new Address[] { new InternetAddress("zhiqiangzhan@gmail.com"),
				new InternetAddress("zhiqiangzhan@163.com")});

		// InternetAddress[] addrs =;
		/*
		 * msg.setRecipients(RecipientType.TO,
		 * InternetAddress.parse("zhiqiangzhan@gmail.com, " +
		 * "zhiqiangzhan@163.com,zizhizhan@gmail.com"));
		 */

		msg.setContent("<a href=\"http://www.google.com\" style=\"color:blue\">Testing</a>", "text/html;charset=gbk");
		Transport.send(msg);
	}

}
