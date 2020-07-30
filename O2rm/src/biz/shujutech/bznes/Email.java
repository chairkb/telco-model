package biz.shujutech.bznes;


import biz.shujutech.util.CryptoRsa;
import javax.mail.Message;
import javax.mail.Session;
import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.util.PlainTextParser;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;


public class Email extends Clasz {
	@ReflectField(type=FieldType.STRING, size=256, displayPosition=10) public static String Sender; // can't use "From" since it's a SQL keyword
	@ReflectField(type=FieldType.STRING, size=256, displayPosition=20) public static String Receiver; // can't use "To" since it's a SQL keyword
	@ReflectField(type=FieldType.STRING, size=255, displayPosition=30) public static String Cc;
	@ReflectField(type=FieldType.STRING, size=256, displayPosition=40) public static String Bcc;
	@ReflectField(type=FieldType.STRING, size=512, displayPosition=50) public static String Subject;
	@ReflectField(type=FieldType.HTML, size=2048, displayPosition=60) public static String BodyHtml;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.EmailAttachment", displayPosition=80) public static String Attachment;
	public static final String SYS_EMAIL_SENDER = "shujutech"; // for gmail, don't use the domain name?

	public static Email CreateEmail(Connection aConn, String aSendTo, String aLoginId, String aEmailSubject, String aEmailBody, String aAttachmentName, String aAttachmentType, String aAttachmentContent) throws Exception {
		Email email = (Email) ObjectBase.CreateObject(aConn, Email.class); // kiv, should retrieve from aWorker for polymorphic SalarySlip?
		email.setSender(SYS_EMAIL_SENDER);
		//email.setReceiver(aWorker.getEmail(EmailTypePerson.Work));
		email.setReceiver(aSendTo);
		email.setCc(aLoginId);
		email.setSubject(aEmailSubject);
		email.createAttachment(aConn, aAttachmentName, aAttachmentContent, aAttachmentType);
		email.setBodyHtml(aEmailBody);
		return email;
	}

	public String mailUser = "shujutech";
	public String mailPassword = "shujutech1234";
	public String mailAuth = "true";
	public String mailServer = "localhost";
	public String mailPort = "2525";
	public String mailStartTLS= "true";

	public Email() throws Exception {
		super();
		this.mailUser = App.GetValue("Systm.mailUser", mailUser);
		this.mailPassword = App.GetValue("Systm.mailPassword", mailPassword);
		this.mailServer = App.GetValue("Systm.mailServer", mailServer);
		this.mailPort = App.GetValue("Systm.mailPort", mailPort);
		this.mailAuth = App.GetValue("Systm.mailAuth", mailAuth);
		this.mailStartTLS = App.GetValue("Systm.mailStartTLS", mailStartTLS);
	}

	public String getSender() throws Exception {
		return((String) this.getValueStr(Sender));
	}

	public void setSender(String aSender) throws Exception {
		this.setValueStr(Sender, aSender);
	}

	public String getReceiver() throws Exception {
		return((String) this.getValueStr(Receiver));
	}

	public void setReceiver(String aReceiver) throws Exception {
		this.setValueStr(Receiver, aReceiver);
	}

	public String getCc() throws Exception {
		return((String) this.getValueStr(Cc));
	}

	public void setCc(String aCc) throws Exception {
		this.setValueStr(Cc, aCc);
	}

	public String getBcc() throws Exception {
		return((String) this.getValueStr(Bcc));
	}

	public void setBcc(String aBcc) throws Exception {
		this.setValueStr(Bcc, aBcc);
	}
	
	public String getSubject() throws Exception {
		return((String) this.getValueStr(Subject));
	}

	public void setSubject(String aSubject) throws Exception {
		this.setValueStr(Subject, aSubject);
	}

	public String getBodyHtml() throws Exception {
		return((String) this.getValueStr(BodyHtml));
	}

	public void setBodyHtml(String aBodyHtml) throws Exception {
		this.setValueStr(BodyHtml, aBodyHtml);
	}

	public FieldObjectBox getAttachment() throws Exception {
		return(this.getFieldObjectBox(Attachment));
	}
		
	public void addAttachment(Connection aConn, EmailAttachment aAttachment) throws Exception {
		this.addValueObject(aConn, Attachment, (Clasz) aAttachment);
	}

	public EmailAttachment createAttachment(Connection aConn, String aName, String aAttachment, String aType) throws Exception {
		EmailAttachment emailAttachment = (EmailAttachment) ObjectBase.CreateObject(aConn, EmailAttachment.class);
		emailAttachment.setName(aName);
		emailAttachment.setType(aType);
		emailAttachment.setAttachment(aAttachment);
		this.addAttachment(aConn, emailAttachment);
		return(emailAttachment);
	}

	public void sendEmail(Connection aConn) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", this.mailStartTLS);
		props.put("mail.smtp.user", this.mailUser);
		props.put("mail.smtp.password", this.mailPassword);
		props.put("mail.smtp.host", this.mailServer);
		props.put("mail.smtp.port", this.mailPort);
		props.put("mail.smtp.auth", this.mailAuth);
		javax.mail.Session session = javax.mail.Session.getInstance(props, null);
		MimeMessage message = new MimeMessage(session);

		InternetAddress from = new InternetAddress(this.getSender());
		message.setFrom(from);
		AddEmailAddr(message, RecipientType.TO, this.getReceiver());
		message.setSubject(this.getSubject());
		
		AddEmailAddr(message, RecipientType.CC, this.getCc());
		AddEmailAddr(message, RecipientType.BCC, this.getBcc());


		String bodyHtml = this.getBodyHtml();
		String bodyText = PlainTextParser.html2PlainText(this.getBodyHtml());
		Multipart multipartAlt = new MimeMultipart("alternative");

		// Plain text
		BodyPart messageText = new MimeBodyPart();
		messageText.setText(bodyText);
		multipartAlt.addBodyPart(messageText);

		// HTML text
		BodyPart messageHtml = new MimeBodyPart();
		messageHtml.setContent(bodyHtml, "text/html");
		multipartAlt.addBodyPart(messageHtml);

		// now place in both the alternative part into a new MimeBodyPart
		MimeBodyPart messagePart = new MimeBodyPart();
		messagePart.setContent(multipartAlt);

		// this is the real master MimeMultipart with message and attachment
		MimeMultipart multipartMaster = new MimeMultipart("related");
		multipartMaster.addBodyPart(messagePart);

		// https://mlyly.wordpress.com/2011/05/13/hello-world/
		StringBuilder sb = new StringBuilder();
		this.getFieldObjectBox(Email.Attachment).resetIterator();
		while (this.getFieldObjectBox(Email.Attachment).hasNext(aConn)) {
			EmailAttachment emailAttachment = (EmailAttachment) this.getFieldObjectBox(Email.Attachment).getNext();
			String id = UUID.randomUUID().toString();
			sb.append("<img src=\"cid:");
			sb.append(id);
			sb.append("\" alt=\"ATTACHMENT\"/>\n");

			MimeBodyPart attachment = new MimeBodyPart();
			int startAt = emailAttachment.getAttachment().indexOf(",") + 1;
			String base64Str = emailAttachment.getAttachment().substring(startAt);
			byte byteAttachment[] = Base64.getMimeDecoder().decode(base64Str);
			ByteArrayDataSource bds = new ByteArrayDataSource(byteAttachment, emailAttachment.getType()); 
			//ByteArrayDataSource bds = new ByteArrayDataSource(emailAttachment.getAttachment().getBytes(), emailAttachment.getType()); 
			attachment.setDataHandler(new DataHandler(bds)); 
			attachment.setHeader("Content-ID", "<" + id + ">");
			attachment.setFileName(emailAttachment.getName());
			multipartMaster.addBodyPart(attachment);
		}

		message.setContent(multipartMaster);
		// Transport.send(message);

		// this is for gmail
		Transport transport = session.getTransport("smtp");
		transport.connect(this.mailServer, this.mailUser, this.mailPassword);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	public static void AddEmailAddr(MimeMessage aMessage, RecipientType aRecipientType, String aStrAddr) throws Exception {
		String[] splittedString = aStrAddr.split(";");
		for (String eachAddr : splittedString) {
			if (eachAddr.isEmpty() == false) {
				InternetAddress iaddr = new InternetAddress(eachAddr);
				aMessage.addRecipient(aRecipientType, iaddr);
			}
		}
	}


	/**
	 * ContactUs
	 */
	private static final String USER_NAME = "shujutech"; // GMail user name (just the part before "@gmail.com")
	private static final String PASSWORD_ENCRYPTED="GhjVEnhS38ZGP2Ju4J3q13KMzwzhFTeP6i59J5sUc873gfLsyzq4x1uZzzSY+dYP8eIKuODxtu1BZfVzEce+QxImnD0ydQpSnpezMZulgP1dlkH3Ui3EyYUZ9/V5D44Ec4/tzNGO0rOkS8kEjxo6OUMXBYz7HajmcHoSRKpeJGM=";
	private static final String RECIPIENT = "kbchair@gmail.com";

	public static void SendFromGmail(String aFrom, String aMsg, String aSubject) throws Exception {
		CryptoRsa cryptoRsa = new CryptoRsa();

		String from = USER_NAME;
		String pass = cryptoRsa.decryptText(PASSWORD_ENCRYPTED);
		String[] to = { RECIPIENT }; // list of recipient email addresses
		String subject = aSubject; 
		String body = aMsg;
		SendFromGmail(from, pass, to, subject, body);
	}

	public static void SendFromGmail(String from, String pass, String[] to, String subject, String body) throws Exception {
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		InternetAddress[] toAddress = new InternetAddress[to.length];

		for( int i = 0; i < to.length; i++ ) { // To get the array of addresses
			toAddress[i] = new InternetAddress(to[i]);
		}

		for (InternetAddress toAddres : toAddress) {
			message.addRecipient(Message.RecipientType.TO, toAddres);
		}

		message.setSubject(subject);
		message.setText(body);
		Transport transport = session.getTransport("smtp");
		transport.connect(host, from, pass);
		transport.sendMessage(message, message.getAllRecipients());
	}
}



