package com.crisquadros.cursomc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.crisquadros.cursomc.dto.ClienteDTO;
import com.crisquadros.cursomc.repositories.ClienteRepository;
import com.crisquadros.cursomc.resources.domain.Cliente;
import com.crisquadros.cursomc.service.exception.DataIntegrityException;
import com.crisquadros.cursomc.service.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Optional<Cliente> find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);	
		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto nao encontrado id: "+id+", tipo: "+Cliente.class.getName()); 
			
		}
		
		return obj;
	}

	/*
	public Cliente update(Cliente obj) {
		find(obj.getId());
		return repo.save(obj);
	} */
	
	/*
	public Cliente update(Cliente obj)
	Cliente newObj = find(obj.getId());
	udateData(newObj, obj);
	return repo.save(newObj); */

	public void delete(Integer id) {
		find(id);
	try {
		repo.deleteById(id);
	}catch(DataIntegrityViolationException e) {
		throw new DataIntegrityException("Cliente com produtos nao pode ser excluida");
	}
		
	}

	public List<Cliente> findAll() {
		return repo.findAll();
		
	}
	
	//paginacao//
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy );
		return repo.findAll(pageRequest);
		
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
}
