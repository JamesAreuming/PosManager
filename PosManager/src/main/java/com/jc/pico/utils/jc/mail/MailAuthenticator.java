package com.jc.pico.utils.jc.mail;

/**
 * <PRE>
 * Filename : MailAuthenticator.java
 * Class    :
 * Function :
 * Comment  :
 * History  : 
 *
 * </PRE>
 * @version 1.0
 * @author  
 * @since   JDK 1.5
 */
public final class MailAuthenticator extends javax.mail.Authenticator {
	// properties
	private String id;
	private String pw;
	
	public MailAuthenticator(String id, String pw) {
		this.id = id;
		this.pw = pw;
	}
	
	protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
		return new javax.mail.PasswordAuthentication(id, pw);
	}
}
