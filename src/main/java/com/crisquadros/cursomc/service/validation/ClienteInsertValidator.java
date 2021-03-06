package com.crisquadros.cursomc.service.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.crisquadros.cursomc.dto.ClienteNewDTO;
import com.crisquadros.cursomc.repositories.ClienteRepository;
import com.crisquadros.cursomc.resources.domain.Cliente;
import com.crisquadros.cursomc.resources.domain.enums.TipoCliente;
import com.crisquadros.cursomc.resources.exception.FieldMessage;
import com.crisquadros.cursomc.service.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

@Autowired
private ClienteRepository repo;	
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getcpfoucnpj())) {
			list.add(new FieldMessage("cpfoucnpj", "CPF inválido"));
		}

		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getcpfoucnpj())) {
			list.add(new FieldMessage("cpfoucnpj", "CNPJ inválido"));
		}

	
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}

