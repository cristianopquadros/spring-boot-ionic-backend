package com.crisquadros.cursomc;

import java.text.SimpleDateFormat;
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
import com.crisquadros.cursomc.repositories.ItemPedidoRepository;
import com.crisquadros.cursomc.repositories.PagamentoRepository;
import com.crisquadros.cursomc.repositories.PedidoRepository;
import com.crisquadros.cursomc.repositories.ProdutoRepository;
import com.crisquadros.cursomc.resources.domain.Categoria;
import com.crisquadros.cursomc.resources.domain.Cidade;
import com.crisquadros.cursomc.resources.domain.Cliente;
import com.crisquadros.cursomc.resources.domain.Endereco;
import com.crisquadros.cursomc.resources.domain.Estado;
import com.crisquadros.cursomc.resources.domain.ItemPedido;
import com.crisquadros.cursomc.resources.domain.Pagamento;
import com.crisquadros.cursomc.resources.domain.PagamentoComBoleto;
import com.crisquadros.cursomc.resources.domain.PagamentoComCartao;
import com.crisquadros.cursomc.resources.domain.Pedido;
import com.crisquadros.cursomc.resources.domain.Produto;
import com.crisquadros.cursomc.resources.domain.enums.EstadoPagamento;
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
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentosRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

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
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 10:00"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2018 00:00"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		
		pagamentosRepository.saveAll(Arrays.asList(pagto1, pagto2));
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1));
		ped2.getItens().addAll(Arrays.asList(ip3));
		ped1.getItens().addAll(Arrays.asList(ip2));
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
		
		
		
		
		
		
		
	}

	
}
