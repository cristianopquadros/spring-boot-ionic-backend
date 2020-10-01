package com.crisquadros.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.crisquadros.cursomc.resources.domain.Cliente;
import com.crisquadros.cursomc.service.validation.ClienteUpdate;

@ClienteUpdate
public class ClienteDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	@NotEmpty(message="nome nao pode ser vazio")
	@Length(message="o tamanho entre 5 e 120 caracteres")
	private String nome;
	
	@NotEmpty(message="email nao pode ser vazio")
	@Email(message="digite um email valido")
	private String email;
	
	public ClienteDTO() {
	}

	public ClienteDTO(Cliente obj) {
		id = obj.getId();
		nome = obj.getNome();
		email = obj.getEmail();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
