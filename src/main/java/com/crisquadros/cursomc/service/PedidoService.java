package com.crisquadros.cursomc.service;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.crisquadros.cursomc.repositories.ItemPedidoRepository;
import com.crisquadros.cursomc.repositories.PagamentoRepository;
import com.crisquadros.cursomc.repositories.PedidoRepository;
import com.crisquadros.cursomc.resources.domain.Categoria;
import com.crisquadros.cursomc.resources.domain.Cliente;
import com.crisquadros.cursomc.resources.domain.ItemPedido;
import com.crisquadros.cursomc.resources.domain.PagamentoComBoleto;
import com.crisquadros.cursomc.resources.domain.Pedido;
import com.crisquadros.cursomc.resources.domain.enums.EstadoPagamento;
import com.crisquadros.cursomc.security.UserSS;
import com.crisquadros.cursomc.service.exception.AuthorizationException;
import com.crisquadros.cursomc.service.exception.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Optional<Pedido> find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto nao encontrado id: "+id+", tipo: "+Categoria.class.getName()); 
			
		}
		
		return obj;
	}
    
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
		PagamentoComBoleto	pagto = (PagamentoComBoleto) obj.getPagamento();
		boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()).get());
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso Negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}

}
