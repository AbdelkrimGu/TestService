package com.example.userservice.Service;
import com.example.userservice.Generators.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {



    @Autowired private JavaMailSender javaMailSender;
    @Autowired private TokenGenerator tokenGenerator;

    @Value("${spring.mail.username}") private String sender;
    public void sendEmail(String email , String url , String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(email);
            message.setSubject("Email Verification");
            message.setText("Please copy this code to validate your email "+token.toUpperCase()+" \n\n\nor click on this link : " + url);

            javaMailSender.send(message);

            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forgetMail(String mail){
        try {
            String token = tokenGenerator.generateToken(mail);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(mail);
            message.setSubject("Password Reset");
            message.setText("Please click on this link to reset your password: http://localhost:8080/api/v1/auth/?token=" + token);

            javaMailSender.send(message);

            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
