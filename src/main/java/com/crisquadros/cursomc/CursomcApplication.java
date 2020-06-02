package com.crisquadros.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.crisquadros.cursomc.repositories.CategoriaRepository;
import com.crisquadros.cursomc.repositories.CidadeRepository;
import com.crisquadros.cursomc.repositories.ClienteRepository;
import com.crisquadros.cursomc.repositories.EnderecoRepository;
import com.crisquadros.cursomc.repositories.EstadoRepository;
import com.crisquadros.cursomc.repositories.ProdutoRepository;
import com.crisquadros.cursomc.resources.domain.Categoria;
import com.crisquadros.cursomc.resources.domain.Cidade;
import com.crisquadros.cursomc.resources.domain.Cliente;
import com.crisquadros.cursomc.resources.domain.Endereco;
import com.crisquadros.cursomc.resources.domain.Estado;
import com.crisquadros.cursomc.resources.domain.Produto;
import com.crisquadros.cursomc.resources.domain.enums.TipoCliente;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informatica");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
				
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		Estado est1 = new Estado(null, "RS");
		Estado est2 = new Estado(null, "SC");
		
		Cidade c1 = new Cidade(null, "Sao Gabriel", est1);
		Cidade c2 = new Cidade(null, "Blumenau", est2);
		Cidade c3 = new Cidade(null, "Porto Alegre", est1);
		
		est1.getCidade().addAll(Arrays.asList(c1,c3));
		est2.getCidade().addAll(Arrays.asList(c2));
		
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		Cliente cli1 = new Cliente(null,"maria silva", "maria@email.com", ",36378945627", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("32323232", "99999999"));
		
		Endereco e1 = new Endereco(null, "Rua Flores","300", "apto1", "agua verde", "89068400", cli1, c1);
		Endereco e2 = new Endereco(null, "Rua x","100", "apto12", "verde", "89068400", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1,e2));
		
	}

	
}
