/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apitechnosoft.ipad.model;

/**
 *
 * @author Kanishk singh
 */
/*
Some SMTP servers require a username and password authentication before you
can use their Server for Sending mail. This is most common with couple
of ISP's who provide SMTP Address to Send Mail.

This Program gives any example on how to do SMTP Authentication
(User and Password verification)

This is a free source code and is provided as it is without any warranties and
it can be used in any your code for free.

Author : Sudhir Ancha
*/

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

/*
  To use this program, change values for the following three constants,

    SMTP_HOST_NAME -- Has your SMTP Host Name
    SMTP_AUTH_USER -- Has your SMTP Authentication UserName
    SMTP_AUTH_PWD  -- Has your SMTP Authentication Password

  Next change values for fields

  emailMsgTxt  -- Message Text for the Email
  emailSubjectTxt  -- Subject for email
  emailFromAddress -- Email Address whose name will appears as "from" address

  Next change value for "emailList".
  This String array has List of all Email Addresses to Email Email needs to be sent to.


  Next to run the program, execute it as follows,

  SendMailUsingAuthentication authProg = new SendMailUsingAuthentication();

*/
public class SendMail
{
//  private static final String SMTP_HOST_NAME = "smtp.gmail.com";
//  private static final String SMTP_AUTH_USER = "webmailipad78@gmail.com";
//  private static final String SMTP_AUTH_PWD  = "welcome@ipad2017";
//
//  private static final String emailMsgTxt      = "Password Reset request from your site.";
//  private static final String emailSubjectTxt  = "Password Reset request from your site";
//  private static final String emailFromAddress = "webmailipad78@gmail.com";
//
//  // Add List of Email address to who email needs to be sent to
//  private static final String[] emailList = {"kanishksingh78@gmail.com,abhishekprasadsingh@gmail.com"};
//
//  public static void main(String args[]) throws Exception
//  {
//    SendMail smtpMailSender = new SendMail();
//    smtpMailSender.postMail( emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress);
//    System.out.println("Sucessfully Sent mail to All Users");
//  }
//
//  public void postMail( String recipients[ ], String subject,
//                            String message , String from) throws MessagingException
//  {
//    boolean debug = false;
//     //Set the host smtp address
//     Properties props = new Properties();
//     props.put("mail.smtp.host", SMTP_HOST_NAME);
//     props.put("mail.smtp.auth", "true");
//     props.put("mail.smtp.starttls.enable","true");
//     props.put("mail.smtp.port", "587");

   
//  private static final String SMTP_HOST_NAME = "mail.teamdeveloper.in";
//  private static final String SMTP_AUTH_USER = "info@teamdeveloper.in";
//  private static final String SMTP_AUTH_PWD  = "welcome@2017";
//
//  private static final String emailMsgTxt      = "Password Reset request from your site.";
//  private static final String emailSubjectTxt  = "Password Reset request from your site";
//  private static final String emailFromAddress = "info@teamdeveloper.in";
//
//  // Add List of Email address to who email needs to be sent to
//  private static final String[] emailList = {"kanishksingh78@gmail.com,abhishekprasadsingh@gmail.com"};
//
//  public static void main(String args[]) throws Exception
//  {
//    SendMail smtpMailSender = new SendMail();
//    smtpMailSender.postMail( emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress);
//    System.out.println("Sucessfully Sent mail to All Users");
//  }
//
//  public void postMail( String recipients[ ], String subject,
//                            String message , String from) throws MessagingException
//  {
//    boolean debug = false;
//
//     //Set the host smtp address
//     Properties props = new Properties();
//     props.put("mail.smtp.host", SMTP_HOST_NAME);
//     props.put("mail.smtp.auth", "true");
//     props.put("mail.smtp.starttls.enable","true");
//    props.put("mail.smtp.port", "25");

      private static final String SMTP_HOST_NAME = "mail.office365.com";
  private static final String SMTP_AUTH_USER = "admin@rxdmedia.com";
  private static final String SMTP_AUTH_PWD  = "Cowboys777!";

  private static final String emailMsgTxt      = "Password Reset request from your site.";
  private static final String emailSubjectTxt  = "Password Reset request from your site";
  private static final String emailFromAddress = "admin@rxdmedia.com";

  // Add List of Email address to who email needs to be sent to
  private static final String[] emailList = {"kanishksingh78@gmail.com"};

  public static void main(String args[]) throws Exception
  {
  //  SendMail smtpMailSender = new SendMail();
   // smtpMailSender.postMail( emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress);
  //  System.out.println("Sucessfully Sent mail to All Users");
  }

  public void postMail( String recipients[ ], String subject, String message , String from) throws MessagingException
  {
    boolean debug = false;

     //Set the host smtp address
     Properties props = new Properties();
     props.put("mail.smtp.host", SMTP_HOST_NAME);
     props.put("mail.smtp.auth", "true");
     props.put("mail.smtp.starttls.enable","true");
     props.put("mail.smtp.port", "587");

    Authenticator auth = new SMTPAuthenticator();
    Session session = Session.getDefaultInstance(props, auth);

    session.setDebug(debug);

    // create a message
    Message msg = new MimeMessage(session);

    // set the from and to address
    InternetAddress addressFrom = new InternetAddress(from);
    msg.setFrom(addressFrom);

    InternetAddress[] addressTo = new InternetAddress[recipients.length];
    for (int i = 0; i < recipients.length; i++)
    {
        addressTo[i] = new InternetAddress(recipients[i]);
    }
    msg.setRecipients(Message.RecipientType.TO, addressTo);


    // Setting the Subject and Content Type
    msg.setSubject(subject);
    msg.setContent(message, "text/html");
    Transport.send(msg);
 }


  
/**
* SimpleAuthenticator is used to do simple authentication
* when the SMTP server requires it.
*/
private class SMTPAuthenticator extends Authenticator
{

        @Override
    public PasswordAuthentication getPasswordAuthentication()
    {
        String username = SMTP_AUTH_USER;
        String password = SMTP_AUTH_PWD;
        return new PasswordAuthentication(username, password);
    }
}

}


