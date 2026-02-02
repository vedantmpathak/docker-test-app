package com.backend.service;

import jakarta.mail.MessagingException;

public interface EmailService {

	void sendMedicalRecord(String email, String subject, String emailBody, byte[] fileData, String fileName) throws MessagingException;

}
