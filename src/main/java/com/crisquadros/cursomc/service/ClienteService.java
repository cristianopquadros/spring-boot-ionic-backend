package com.crisquadros.cursomc.service;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Autowired
	private ImageService imageService; 
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;
	
	
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if(user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		
		Cliente obj = repo.findById(id).get();	
		
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

	/*
	public Cliente update(Cliente obj) {
		find(obj.getId());
		return repo.save(obj);
	}  */
	
	
	
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	} 

	public void delete(Integer id) {
		find(id);
	try {
		repo.deleteById(id);
	}catch(DataIntegrityViolationException e) {
		throw new DataIntegrityException("Cliente com produtos nao pode ser excluida");
	}
		
	}

	
	public Cliente findByEmail(String email) {
		
		UserSS user = UserService.authenticated();
		if(user==null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		
		Cliente obj = repo.findById(user.getId()).get();	
		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto nao encontrado id: "+user.getId()+", Tipo: "+Cliente.class.getName()); 
			
		}
		
		return obj;
		
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
	
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	} 
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("acesso NEgado");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		
		
		String fileName = prefix + user.getId()+".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
		
	
	}
}
