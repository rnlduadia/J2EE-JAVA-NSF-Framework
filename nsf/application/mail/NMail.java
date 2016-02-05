package nsf.application.mail;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * <pre>
 *   
 *     JAVA 로 Mail 을 전송하는 Utility Class.
 *     컴파일 및 실행을 위해서는 JAVA mail 패키지 ( mail.jar ) 와
 *     JAVA Activatiion Framework 의 패키지( activation.jar )가 필요하다. 
 *   
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NMail {
    public String fixKorean(String param){
        try{
            return new String(param.getBytes("UTF-8"),"UTF-8");
        }
        catch (Exception e){
            return null;
        }
      }
      public void sendEmail(String from, String to, String cc, String subject, String content, String attachFile)
        throws Exception {
          
          Properties props = System.getProperties();
          props.put("mail.smtp.host", "10.62.8.199");
          Session mailSession = Session.getDefaultInstance(props, null);
          Message msg = new MimeMessage(mailSession);
          msg.setFrom(new InternetAddress(from));
          msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
          
          if(!cc.trim().equals("")) {
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
          }
          
          msg.setSubject(subject);
          
          if(!attachFile.trim().equals("")) {
              
              BodyPart messageBodyPart = new MimeBodyPart();
              messageBodyPart.setText(content);
              Multipart multipart = new MimeMultipart();
              multipart.addBodyPart(messageBodyPart);
              messageBodyPart = new MimeBodyPart();
              DataSource source = new FileDataSource(attachFile);
              messageBodyPart.setDataHandler(new DataHandler(source));
              messageBodyPart.setFileName(attachFile);
              multipart.addBodyPart(messageBodyPart);
              msg.setContent(multipart);
          }
          else{
              msg.setHeader("Content-type", "text/plain; charset=UTF-8");
              msg.setContent(content,"TEXT/PLAIN; charset=UTF-8");
              msg.setText(content);
          }
          
          msg.setSentDate(new Date());
             
          Transport.send(msg);
      }
}

