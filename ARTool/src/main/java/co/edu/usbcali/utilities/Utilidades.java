package co.edu.usbcali.utilities;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Utilidades {



	private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Validate given email with regular expression.
	 * 
	 * @param email
	 *            email for validation
	 * @return true valid email, otherwise false
	 */
	public static boolean validateEmail(String email) {

		// Compiles the given regular expression into a pattern.
		Pattern pattern = Pattern.compile(PATTERN_EMAIL);

		// Match the given input against this pattern
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();

	}


	public void mandarCorreo(String CorreoEnviado,String texto) {  
		// El correo gmail de envío  
		String correoEnvia = "softwareartool@gmail.com";  
		String claveCorreo = "artool123";  

		// La configuración para enviar correo  
		Properties properties = new Properties();  
		properties.put("mail.smtp.host", "smtp.gmail.com");  
		properties.put("mail.smtp.starttls.enable", "true");  
		properties.put("mail.smtp.port", 587);  
		properties.put("mail.smtp.auth", "true");  
		properties.put("mail.user", correoEnvia);  
		properties.put("mail.password", claveCorreo);  

		// Obtener la sesion  
		Session session = Session.getInstance(properties, null);  

		try {  
			// Crear el cuerpo del mensaje  
			MimeMessage mimeMessage = new MimeMessage(session);  

			// Agregar quien envía el correo  
			mimeMessage.setFrom(new InternetAddress(correoEnvia, "Software ARTOOL"));  

			// Los destinatarios  
			InternetAddress internetAddresses = new InternetAddress(CorreoEnviado) ;  

			// Agregar los destinatarios al mensaje  
			mimeMessage.addRecipient(Message.RecipientType.TO,  
					internetAddresses);  

			// Agregar el asunto al correo  
			mimeMessage.setSubject("Bienvenido a la herramienta ARTool");  

			// Creo la parte del mensaje  
			MimeBodyPart mimeBodyPart = new MimeBodyPart();  
			mimeBodyPart.setText(texto);  

			// Crear el multipart para agregar la parte del mensaje anterior  
			Multipart multipart = new MimeMultipart();  
			multipart.addBodyPart(mimeBodyPart);  

			// Agregar el multipart al cuerpo del mensaje  
			mimeMessage.setContent(multipart);  

			// Enviar el mensaje  
			Transport transport = session.getTransport("smtp");  
			transport.connect(correoEnvia, claveCorreo);  
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());  
			transport.close();  


		} catch (Exception ex) {  
			ex.printStackTrace();  
			System.out.println(ex.getMessage());
		}  
	}
}
