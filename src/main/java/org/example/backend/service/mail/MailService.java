package org.example.backend.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String userEmail, String code) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(userEmail);
        simpleMailMessage.setSubject("[E-commerce] 이메일 인증 코드");
        simpleMailMessage.setText("인증번호는 " + code + " 입니다.");
        javaMailSender.send(simpleMailMessage);
    }
}
