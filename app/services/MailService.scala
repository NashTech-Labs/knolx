package services


import java.util.{Date, Properties}
import javax.mail.internet.{MimeMessage, InternetAddress}
import javax.mail._
import org.apache.commons.mail.{DefaultAuthenticator, SimpleEmail}

object MailService{

  def sendMail(to: List[String], subject: String, message: String) = {
    to.map {recipient =>
    val email = new SimpleEmail()
    email.setHostName("smtp.googlemail.com")
    email.setSmtpPort(465)
    email.setAuthenticator(new DefaultAuthenticator("rahulsocialapp@gmail.com", "rough@45"))
    email.setSSLOnConnect(true)
    email.setFrom("rahulsocialapp@gmail.com")
    email.setSubject(subject)
    email.setMsg(message)
    email.addTo(recipient)
      email.send()
}
  }

  def sendHtmlEmail(to: List[String], subject: String, message: String) {
    to.map { recipient =>

      val mailFrom = "rahulsocialapp@gmail.com";
      val password = "rough@45";
      val properties = new Properties();
      properties.put("mail.smtp.host", "smtp.gmail.com");
      properties.put("mail.smtp.port", "587");
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.starttls.enable", "true");
      val auth = new Authenticator() {
        override def getPasswordAuthentication(): PasswordAuthentication = {
          return new PasswordAuthentication(mailFrom, password);
        }
      };
      val session = Session.getInstance(properties, auth);
      val msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(mailFrom));
      val toAddresses = new InternetAddress(recipient)
      msg.setRecipients(Message.RecipientType.TO, toAddresses.toString);
      msg.setSubject(subject);
      msg.setSentDate(new Date());
      msg.setContent(message, "text/html");
      Transport.send(msg);
    }
  }

}
