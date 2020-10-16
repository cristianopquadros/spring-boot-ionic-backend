package com.crisquadros.cursomc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.crisquadros.cursomc.repositories.CategoriaRepository;
import com.crisquadros.cursomc.repositories.ProdutoRepository;
import com.crisquadros.cursomc.resources.domain.Categoria;
import com.crisquadros.cursomc.resources.domain.Produto;
import com.crisquadros.cursomc.service.exception.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Optional<Produto> find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto nao encontrado id: "+id+", tipo: "+Categoria.class.getName()); 
			
		}
		
		return obj;
	}

	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoiasIn(nome, categorias, pageRequest);
	}
	
}
