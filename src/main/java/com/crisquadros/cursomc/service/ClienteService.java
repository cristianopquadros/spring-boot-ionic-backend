package com.crisquadros.cursomc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crisquadros.cursomc.repositories.ClienteRepository;
import com.crisquadros.cursomc.resources.domain.Cliente;
import com.crisquadros.cursomc.service.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Optional<Cliente> buscar(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto nao encontrado id: "+id+", tipo: "+Cliente.class.getName()); 
			
		}
		
		return obj;
	}

}
