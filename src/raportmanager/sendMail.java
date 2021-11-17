/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raportmanager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Arek
 */
public interface sendMail {
    
    
                String fromEmail =FXMLDocumentController.rS.qP.reader.emailFrom;//"arkadiusz.michacz@mspit.nazwa.pl";//"kontodotestu93@gmail.com"; //requires valid gmail id
		String password = FXMLDocumentController.rS.qP.reader.emailPasswd;//"Jk2!2$Jme!2awA";//"dotestukonto93"; // correct password for gmail id
		String [] toEmail = {"arkadiusz.michacz@gmail.com","amadiszkola@gmail.com"}; // can be any email id 
                //String toEmail = "arkadiusz.michacz@gmail.com";
                //MimeMessage emailMessage;
                //Session session;
                Properties props = new Properties();
                
                String emailSubject = "Raport Email";
		String emailBody = "This is an email sent by JavaMail api thru SSL.";
                
                
                
                public static void setMailServerProperties() {

		

		System.out.println("SSLEmail Start");
		
		props.put("mail.smtp.host",FXMLDocumentController.rS.qP.reader.smtp/*"smtp.mspit.nazwa.pl""smtp.gmail.com"*/); //SMTP Host
		props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
		props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
		props.put("mail.smtp.port", "465"); //SMTP Port 
                props.put("mail.smtp.connectiontimeout","5000");
                props.put("mail.smtp.timeout","5000");

                }
                
                
                public static void createEmailMessage(String nazwaPliku, String[] recipients) throws IOException {
                    
                        Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
                        
                        @Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                            
				return new javax.mail.PasswordAuthentication(fromEmail, password);
			}
                        };

                         Session session = Session.getDefaultInstance(props, auth);
                        System.out.println("Session created");
                    
                    
                    
                            try {
                            //excelCreator.formatujNazwe(nazwaPliku);
                            
                            /*LocalDate date = LocalDate.now();
                            System.out.println(date);
                            String time = LocalTime.now().toString();
                            String [] czas = time.split("\\.", 2);
                            czas = czas[0].split(":",3);
                            time = czas[0]+"-"+czas[1];*/
                            
                            
                            
                            
                            MimeMessage emailMessage = new MimeMessage(session);
                            
                            System.out.println("MimeMessage");

                            emailMessage.setFrom(fromEmail);
                            //emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                            
                            
                            for (int i = 0; i < recipients.length; i++) {
                                emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipients[i]));
                            }
                            


                            emailMessage.setSubject(emailSubject);
                            //emailMessage.setContent(emailBody, "text/html");//for a html email
                            emailMessage.setText(emailBody);// for a text email
                            
                            BodyPart attach = new MimeBodyPart();
                            
                            attach.setText("Raport "+nazwaPliku);
                            
                            Multipart mltp = new MimeMultipart();
                            
                            mltp.addBodyPart(attach);
                            
                            attach = new MimeBodyPart();
                            
                            String path = RaportManager.class.getProtectionDomain()
                                    .getCodeSource().getLocation().getPath();
                            
                            String filename = path.replace("RaportManager.jar", "taski/"+nazwaPliku);
                            filename = filename.replace("/", "\\");
                            filename = filename.replace("%20", " ");
                            //String filename = "F:\\projekty\\JavaFXApplicationMigrator\\"+nazwaPliku+" "+date+" "+time+".xls";
                            //String filename = "F:\\test1.xlsx";
                            //String filename = "C:\\Users\\mspit\\"+nazwaPliku+" "+date+" "+time+".xls";
                            
                            DataSource dtsrc = new FileDataSource(filename); 
                            attach.setDataHandler(new DataHandler(dtsrc));
                            attach.setFileName(nazwaPliku);
                            mltp.addBodyPart(attach);
                            
                            
                            emailMessage.setContent(mltp);

                            

                            Transport.send(emailMessage);

                            System.out.println("Done");

                        } catch (MessagingException e) {
                            System.err.println(e.getMessage());
                            throw new RuntimeException(e);
                            
                        }

                }
                
                public static void auth(){
                    
                    Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
                        
                        @Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                            
				return new javax.mail.PasswordAuthentication(fromEmail, password);
			}
                        };

                         Session session = Session.getDefaultInstance(props, auth);
                        System.out.println("Session created");
                
                
                }
                
              
}
