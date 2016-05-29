package eduportal.model;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class MailSender {

	
	public static void sendAccountCreation (String dest, String pass, String name) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress("accounts@eduportal-1277.appspotmail.com", "VTG Study"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(dest, "Mr. User"));
			message.setHeader("Content-Type", "text/plain; charset=UTF-8");
			message.setSubject("Welcome to Vedi Tour Group");
			message.setText("Уважаемый " + name + "\n\nДобро пожаловать в Vedi Tour Group, подразделение обучения за границей. Для Вас был создан аккаунт в личном кабинете по адресу: https://eduportal-1277.appspot.com \n\n" + 
			"В качестве логина используйте указанный номер телефона или текущий Ваш адрес электронной почты. Пароль для вас сгенерирован системой автоматически: " + pass + "\nСмените его при первом же входе в систему.\n\nС уважением, Vedi Tour Group");
			
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
