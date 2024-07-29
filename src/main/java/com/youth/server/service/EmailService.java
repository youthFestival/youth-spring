package com.youth.server.service;

import com.youth.server.exception.BusinessLogicException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

//    public void sendEmail(String toEmail, String title, String text) {
//        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
//        try {
//            javaMailSender.send(emailForm);
//        } catch (RuntimeException e) {
//            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
//                    "title: {}, text: {}", toEmail, title, text);
//            throw new BusinessLogicException(BusinessLogicException.ExceptionCode.UNABLE_TO_SEND_EMAIL);
//        }
//    }
//
//    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject(title);
//        message.setText(text);
//
//        return message;
//    }
    public void sendEmail(String toEmail, String title, String text) {
        MimeMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            javaMailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw new BusinessLogicException(BusinessLogicException.ExceptionCode.UNABLE_TO_SEND_EMAIL);
        }
    }

        private MimeMessage createEmailForm(String toEmail, String title, String text) {
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(toEmail);
                helper.setSubject(title);
                helper.setText(text);
                helper.setFrom(new InternetAddress("wogks119@gmail.com", "YouthSupport")); // 발신자 이름을 포함하여 설정
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException("Failed to create email form", e);
            }

            return message;
        }
}
