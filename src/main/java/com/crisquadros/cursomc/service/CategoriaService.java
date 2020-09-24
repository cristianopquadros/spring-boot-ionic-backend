package com.crisquadros.cursomc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crisquadros.cursomc.repositories.CategoriaRepository;
import com.crisquadros.cursomc.resources.domain.Categoria;
import com.crisquadros.cursomc.service.exception.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Optional<Categoria> find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto nao encontrado id: "+id+", tipo: "+Categoria.class.getName()); 
			
		}
		
		return obj;
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}

}
