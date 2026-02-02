package com.backend.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

	@Override
	public void sendMedicalRecord(String email, String subject, String emailBody, byte[] fileData, String fileName) throws MessagingException {
		// TODO Auto-generated method stub
		MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(emailBody);

        helper.addAttachment(
                fileName,
                new ByteArrayResource(fileData)
        );

        mailSender.send(message);
	}

    
}
