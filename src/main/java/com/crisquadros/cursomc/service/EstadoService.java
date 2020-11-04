package com.crisquadros.cursomc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crisquadros.cursomc.repositories.EstadoRepository;
import com.crisquadros.cursomc.resources.domain.Estado;

@Service
public class EstadoService {
	
	@Autowired 
	private EstadoRepository repo;
	
	public List<Estado> findAll() {
		return repo.findAllByOrderByNome();
	}

}
