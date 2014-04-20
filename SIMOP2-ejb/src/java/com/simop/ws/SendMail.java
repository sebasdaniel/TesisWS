/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.simop.ws;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author 53B45
 */
public class SendMail {

    public void send(String clave) {
        try {
            // Propiedades de la conexión
            Properties props = new Properties();
            
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", "proyecto.simop@gmail.com");
            props.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("proyecto.simop@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("geraldinepmontes@gmail.com"));
            message.setSubject("Recuperar Contraseña");
            message.setText("Su contraseña es: " + clave);

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect("proyecto.simop@gmail.com", "ingenieria1.618");
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
            
        } catch (MessagingException e) {
            System.err.println(e);
        }

    }
}
