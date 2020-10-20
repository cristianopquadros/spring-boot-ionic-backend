package com.crisquadros.cursomc.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.crisquadros.cursomc.dto.ClienteDTO;
import com.crisquadros.cursomc.dto.ClienteNewDTO;
import com.crisquadros.cursomc.repositories.ClienteRepository;
import com.crisquadros.cursomc.repositories.EnderecoRepository;
import com.crisquadros.cursomc.resources.domain.Cidade;
import com.crisquadros.cursomc.resources.domain.Cliente;
import com.crisquadros.cursomc.resources.domain.Endereco;
import com.crisquadros.cursomc.resources.domain.enums.Perfil;
import com.crisquadros.cursomc.resources.domain.enums.TipoCliente;
import com.crisquadros.cursomc.security.UserSS;
import com.crisquadros.cursomc.service.exception.AuthorizationException;
import com.crisquadros.cursomc.service.exception.DataIntegrityException;
import com.crisquadros.cursomc.service.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repo;
	
	/*
	@Autowired
	private CidadeRepository cidadeRepository; */
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Optional<Cliente> find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if(user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		
		Optional<Cliente> obj = repo.findById(id);	
		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto nao encontrado id: "+id+", tipo: "+Cliente.class.getName()); 
			
		}
		
		return obj;
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	
	public Cliente update(Cliente obj) {
		find(obj.getId());
		return repo.save(obj);
	}  
	
	/*
	
	
	public Optional<Cliente> update(Optional<Cliente> obj) {
		Optional<Cliente> newObj = find(obj.get());
		updateData(newObj, obj);
		return repo.saveAll(newObj);
	} */

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
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
		
	/*	return new Cliente(
		        objDto.getId(), 
		        objDto.getNome(), 
		        objDto.getEmail(), 
		        find(objDto.getId()).getCpfOuCnpj(), 
		        find(objDto.getId()).getTipo()); */
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente (null,
				objDto.getNome(), 
				objDto.getEmail(),
				objDto.getcpfoucnpj(), 
				TipoCliente.toEnum(objDto.getTipo()),
				pe.encode(objDto.getSenha()));
		
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if(objDto.getTelefone2()!=null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if(objDto.getTelefone3()!=null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}
	
	/*
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	} */
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		return s3Service.uploadFile(multipartFile);
	}
}
