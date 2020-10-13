package com.crisquadros.cursomc.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.crisquadros.cursomc.resources.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
    void sendHtmlEmail(MimeMessage msg);

	void sendOrderConfirmationHtmlEmail(Pedido obj);
	

}
