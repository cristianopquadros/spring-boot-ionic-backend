package com.crisquadros.cursomc.service;

import org.springframework.mail.SimpleMailMessage;

import com.crisquadros.cursomc.resources.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
