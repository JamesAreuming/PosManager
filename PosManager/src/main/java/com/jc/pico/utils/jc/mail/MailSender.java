/*
 * Filename : MailSender.java
 * Class    :
 * Function :
 * Comment  :
 * History  : 
 * 			  
 */

package com.jc.pico.utils.jc.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * 
 * @version 1.0
 * @author  
 * @since   JDK 1.6
 */
public class MailSender {
	// constants
	public static final String TYPE_TEXT_PLAIN = "text/plain";
	public static final String TYPE_TEXT_HTML = "text/html";
	
	public static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	public static final String PROTOCOL = "smtp";
	
	private Session session;
	
	// smtp host properties
	private String host;
	private String id;
	private String password;
	private boolean isUseSSL;
	private String port;
	
	private String from;
	private String fromAlias;
	
	// mail information
	private String to;
	private String toAlias;
	private String cc;
	private String bcc;
	private String subject;
	private String text;
	private String type;
	private String charset;
	private String[] fileNames;
	
	// mail header
	private String messageId;
	private String references;
	private String inReplyTo;
	private String replyTo;
	//---------------------------------------------------------------------------
	// construtor
	//---------------------------------------------------------------------------
	
	public MailSender(String host) {
		this(host, null, null);
	}
	
	public MailSender(String host, String id, String pw) {
		setHost(host);
		setId(id);
		setPassword(pw);
	}
	
	public MailSender(String host, String id, String pw, String port) {
    setHost(host);
    setId(id);
    setPassword(pw);
    setPort(port);
  }
	
	public void connect() {
	  com.jc.pico.utils.jc.mail.MailAuthenticator auth = null;
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", PROTOCOL);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.localhost", host);
		props.put("mail.smtp.port", port);
		if (isUseSSL) {
			props.put("mail.smtp.starttls.enable","true");
			props.put("mail.smtp.socketFactory.port", port);
			props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.put("mail.smtp.socketFactory.fallback", "false");
		}
		
		if (id != null && password != null) {
			props.put("mail.smtp.auth", "true");
			auth = new com.jc.pico.utils.jc.mail.MailAuthenticator(id, password);
		} else {props.put("mail.smtp.auth", "false");}
		
		session = Session.getInstance(props, auth);
	}
	
	//---------------------------------------------------------------------------
	// send
	//---------------------------------------------------------------------------
	
	public void send() throws Exception {
		connect();
		MimeMessage message = new MimeMessage(session);
		InternetAddress addr = new InternetAddress(from, fromAlias, charset);
		Message.RecipientType rType = null;
		
		// set message
		message.setFrom(addr); 
		message.setSubject(subject, charset);
		message.setContent(text, type + "; charset=\"" + charset + "\"");
		message.setSentDate(new Date());

		// set TO
		rType = Message.RecipientType.TO;
		if (toAlias != null && toAlias.length() > 0) {
			message.addRecipient(rType, new InternetAddress(to, toAlias, charset));
		} else {
			message = addRecipients(message, rType, to);
		}

		// set CC
		if (cc != null && cc.length() > 0) {
			rType = Message.RecipientType.CC;
			message = addRecipients(message, rType, cc);
		}
		
		// set BCC
		if (bcc != null && bcc.length() > 0) {
			rType = Message.RecipientType.BCC;
			message = addRecipients(message, rType, bcc);
		}

		// set attached files
		if (fileNames != null) {
			// set multi part
			Multipart multipart = new MimeMultipart();         
			     
			// set body part & add multi part
			MimeBodyPart messageBodyPart = new MimeBodyPart(); 
			messageBodyPart.setContent(text, type + "; charset=\"" + charset + "\"");
			multipart.addBodyPart(messageBodyPart); 
		
			// set attach file body part & add multi part
			java.io.File file = null;
			BodyPart fileBodyPart = null;
			DataSource source = null;
			for (int i = 0; i < fileNames.length; i++) {
				file = new java.io.File(fileNames[i]);
				if (file.isFile()) {
					fileBodyPart = new MimeBodyPart(); 
					source = new FileDataSource(fileNames[i]);
					fileBodyPart.setDataHandler(new DataHandler(source));
					fileBodyPart.setFileName(MimeUtility.encodeText(new File(fileNames[i]).getName(), charset, "B"));
					
					multipart.addBodyPart(fileBodyPart);
				}
			}
			// set content
			message.setContent(multipart); 
		}

		// set header
		if (references != null && references.length() > 0) {
			message.setHeader("References", references);
		}
		if (inReplyTo != null && inReplyTo.length() > 0) {
			message.setHeader("In-Reply-To", inReplyTo);
		}
		if (replyTo != null && replyTo.length() > 0) {
			message.setHeader("Reply-To", replyTo);
		}
		
		// send message
		if (messageId != null && messageId.length() > 0) {
			message.saveChanges();
			message.setHeader("Message-ID", messageId);

			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();			
		} else {
			Transport.send(message);
		}
		
	}
	
	public MimeMessage addRecipients(MimeMessage message, Message.RecipientType type, String recipient) throws Exception {
		StringTokenizer st = null;
		String delim = null;
		
		if (recipient.indexOf(";") > -1 || recipient.indexOf(",") > -1) {
			if (recipient.indexOf(";") > -1) {delim = ";";}
			else if (recipient.indexOf(",") > -1) {delim = ",";}
			st = new StringTokenizer(recipient, delim);
			while (st.hasMoreTokens()) {message = addRecipient(message, type, st.nextToken());}
		} else {
			message = addRecipient(message, type, recipient);
		}

		return message;
	}
	
	public MimeMessage addRecipient(MimeMessage message, Message.RecipientType type, String recipient) throws Exception {
		String alias = null;
		recipient = recipient.trim();

		if (recipient.indexOf("<") > -1) {
			alias = recipient.substring(0, recipient.indexOf("<")).trim();
			recipient = recipient.substring(recipient.indexOf("<") + 1, recipient.length() - 1);
		}
		message.addRecipient(type, new InternetAddress(recipient, alias, charset));

		return message;
	}

	//---------------------------------------------------------------------------
	// get / set methods
	//---------------------------------------------------------------------------
	
	public String getHost() {return this.host;}
	public void setHost(String host) {this.host = host;}
	
	public String getId() {return this.id;}
	public void setId(String id) {this.id = id;}
	
	public String getPassword() {return this.password;}
	public void setPassword(String password) {this.password = password;}
	
	public boolean isUseSSL() {return this.isUseSSL;}
	public void setUseSSL(boolean isUseSSL) {this.isUseSSL = isUseSSL;}
	
	public String getPort() {return this.port;}
	public void setPort(String port) {this.port = port;}
	
	public String getFrom() {return this.from;}
	public void setFrom(String from) {this.from = from;}
	
	public String getFromAlias() {return this.fromAlias;}
	public void setFromAlias(String fromAlias) {this.fromAlias = fromAlias;}
	
	public String getTo() {return this.to;}
	public void setTo(String to) {this.to = to;}
	
	public String getToAlias() {return this.toAlias;}
	public void setToAlias(String toAlias) {this.toAlias = toAlias;}	
	
	public String getCc() {return this.cc;}
	public void setCc(String cc) {this.cc = cc;}
	
	public String getBcc() {return this.bcc;}
	public void setBcc(String bcc) {this.bcc = bcc;}
	
	public String getSubject() {return this.subject;}
	public void setSubject(String subject) {this.subject = subject;}
	
	public String getText() {return this.text;}
	public void setText(String text) {this.text = (text == null ? "" : text + "\r\n");}
	
	public String getType() {return this.type;}
	public void setType(String type) {this.type = type;}
	
	public String getCharset() {return this.charset;}
	public void setCharset(String charset) {this.charset = charset;}
	
	public String[] getFileNames() {return this.fileNames;}
	public void setFileNames(String[] fileNames) {this.fileNames = fileNames;}
	
	public String getMessageId() {return this.messageId;}
	public void setMessageId(String messageId) {this.messageId = messageId;}
	
	public String getReferences() {return this.references;}
	public void setReferences(String references) {this.references = references;}
	
	public String getInReplyTo() {return this.inReplyTo;}
	public void setInReplyTo(String inReplyTo) {this.inReplyTo = inReplyTo;}
	
	public String getReplyTo() {return this.replyTo;}
	public void setReplyTo(String replyTo) {this.replyTo = replyTo;}
} 
