package br.com.discos.repository.query.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.discos.dto.ItemVendaDetalheDTO;
import br.com.discos.dto.ItemVendaRegistradaDTO;
import br.com.discos.dto.TotalItemVendaDTO;
import br.com.discos.model.Cashback;
import br.com.discos.model.Disco;
import br.com.discos.model.Genero;
import br.com.discos.model.ItemVenda;
import br.com.discos.model.Venda;
import br.com.discos.repository.CashbackRepository;
import br.com.discos.repository.DiscoRepository;
import br.com.discos.repository.GeneroRepository;
import br.com.discos.repository.ItemVendaRepository;
import br.com.discos.repository.VendaRepository;
import br.com.discos.teste.util.GeracaoDadosUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ItemVendaRepositoryQueryImplTest {

	@Autowired
	private DiscoRepository discoRepository;
	
	@Autowired
	private GeneroRepository generoRepository;
	
	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private ItemVendaRepository itemVendaRepository;
	
	@Autowired
	private CashbackRepository cashbackRepository;
	
	private List<Genero> generos = new ArrayList<Genero>();
	private List<Disco> discos = new ArrayList<Disco>();
	private List<Cashback> cashbacks = new ArrayList<Cashback>();
	private List<Venda> vendas = new ArrayList<Venda>();
	
	@BeforeEach
	public void setUp() {
		generos = GeracaoDadosUtil.criarGeneros(3);
		generoRepository.saveAll(generos);
		
		discos = GeracaoDadosUtil.criarDiscos(50, generos);
		discoRepository.saveAll(discos);
		
		cashbacks = GeracaoDadosUtil.criarCashbacks(generos);
		cashbackRepository.saveAll(cashbacks);
		
		vendas = GeracaoDadosUtil.criarVendas(20, discos, cashbacks);
		vendaRepository.saveAll(vendas);
	}
	
	@AfterEach
	public void tearDown() {
		vendaRepository.deleteAll();
		discoRepository.deleteAll();
		cashbackRepository.deleteAll();
		generoRepository.deleteAll();
	}
	
	@Test
	public void buscandoItensPorCodigoVenda() throws Exception {
		Venda vendaBaseTeste = vendas.get(0);
		List<ItemVendaDetalheDTO> dto = itemVendaRepository.listarItensVendaPorCodigoVenda(vendaBaseTeste.getCodigo());
		assertEquals(vendaBaseTeste.getItensVenda().size(), dto.size());
		dto.forEach(d ->{
			ItemVenda itemBaseTeste = vendaBaseTeste.getItensVenda().stream().filter(f -> f.getCodigo() == d.getCodigo()).findFirst().get();
			Disco discoBaseTeste = discos.stream().filter(f -> f.getCodigo() == itemBaseTeste.getDisco().getCodigo()).findFirst().get();
			Cashback cashbackBaseTeste = cashbacks.stream().filter(f -> f.getCodigo() == itemBaseTeste.getCashback().getCodigo()).findFirst().get();
			BigDecimal valorTotalItem = itemBaseTeste.getValorUnitario().multiply(new BigDecimal(itemBaseTeste.getQuantidade())).setScale(2, RoundingMode.HALF_DOWN);
			assertEquals(itemBaseTeste.getCodigo(), d.getCodigo());
			assertEquals(itemBaseTeste.getQuantidade(), d.getQuantidade());
			assertEquals(itemBaseTeste.getValorUnitario(), d.getValorUnitario());
			assertEquals(itemBaseTeste.getPorcentagemCashback(), d.getPorcentagemCashback());
			assertEquals(discoBaseTeste.getCodigo(), d.getCodigoDisco());
			assertEquals(discoBaseTeste.getNome(), d.getNomeDisco());
			assertEquals(cashbackBaseTeste.getCodigo(), d.getCodigoCashback());
			assertEquals(cashbackBaseTeste.getDia(), d.getDia());
			assertEquals(valorTotalItem, d.getValorTotal());
			assertEquals(itemBaseTeste.getPorcentagemCashback().multiply(valorTotalItem).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN), d.getValorCashback());
		});
		List<ItemVendaDetalheDTO> listaComparacao = dto.stream().sorted(Comparator.comparing(ItemVendaDetalheDTO::getNomeDisco)).collect(Collectors.toList());
		assertEquals(listaComparacao, dto);
	}
	
	@Test
	public void quandoInformadoCodigoVendaInexistente_RetornarNulo() throws Exception {
		assertTrue(itemVendaRepository.listarItensVendaPorCodigoVenda(vendas.get(vendas.size() - 1).getCodigo() + 1).isEmpty());
	}
	
	@Test
	public void buscandoItensVendaRecemRegistrada() throws Exception {
		Venda vendaBaseTeste = vendas.get(0);
		List<ItemVendaRegistradaDTO> dto = itemVendaRepository.listarItensVendaRecemRegistrada(vendaBaseTeste.getCodigo());
		assertEquals(vendaBaseTeste.getItensVenda().size(), dto.size());
		dto.forEach(d ->{
			ItemVenda itemBaseTeste = vendaBaseTeste.getItensVenda().stream().filter(f -> f.getCodigo() == d.getCodigo()).findFirst().get();
			Disco discoBaseTeste = discos.stream().filter(f -> f.getCodigo() == itemBaseTeste.getDisco().getCodigo()).findFirst().get();
			Cashback cashbackBaseTeste = cashbacks.stream().filter(f -> f.getCodigo() == itemBaseTeste.getCashback().getCodigo()).findFirst().get();
			BigDecimal valorTotalItem = itemBaseTeste.getValorUnitario().multiply(new BigDecimal(itemBaseTeste.getQuantidade())).setScale(2, RoundingMode.HALF_DOWN);
			assertEquals(itemBaseTeste.getCodigo(), d.getCodigo());
			assertEquals(itemBaseTeste.getQuantidade(), d.getQuantidade());
			assertEquals(itemBaseTeste.getValorUnitario(), d.getValorUnitario());
			assertEquals(itemBaseTeste.getPorcentagemCashback(), d.getPorcentagemCashback());
			assertEquals(discoBaseTeste.getCodigo(), d.getCodigoDisco());
			assertEquals(discoBaseTeste.getNome(), d.getNomeDisco());
			assertEquals(cashbackBaseTeste.getCodigo(), d.getCodigoCashback());
			assertEquals(cashbackBaseTeste.getDia(), d.getDia());
			assertEquals(valorTotalItem, d.getValorTotal());
			assertEquals(itemBaseTeste.getPorcentagemCashback().multiply(valorTotalItem).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN), d.getValorCashback());
		});
		List<ItemVendaRegistradaDTO> listaComparacao = dto.stream().sorted(Comparator.comparing(ItemVendaRegistradaDTO::getNomeDisco)).collect(Collectors.toList());
		assertEquals(listaComparacao, dto);
	}
	
	@Test
	public void quandoInformadoCodigoVendaRecemRegistradaInexistente_RetornarNulo() throws Exception {
		assertTrue(itemVendaRepository.listarItensVendaRecemRegistrada(vendas.get(vendas.size() - 1).getCodigo() + 1).isEmpty());
	}
	
	@Test
	public void verificandoTotaisVenda() throws Exception {
		Venda vendaBaseTeste = vendas.get(0);
		List<ItemVenda> itensVenda = vendaBaseTeste.getItensVenda();
		BigDecimal totalVenda = BigDecimal.ZERO;
		BigDecimal totalCashback = BigDecimal.ZERO;
		for(int i = 0; i < itensVenda.size(); i++) {
			BigDecimal valorVenda = itensVenda.get(i).getValorUnitario().multiply(new BigDecimal(itensVenda.get(i).getQuantidade())).setScale(2, RoundingMode.HALF_DOWN); 
			totalVenda = totalVenda.add(valorVenda);
			totalCashback = totalCashback.add(valorVenda.multiply(itensVenda.get(i).getPorcentagemCashback()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN));
		}
		List<TotalItemVendaDTO> totais = itemVendaRepository.buscarTotaisItemVenda(vendaBaseTeste.getCodigo());
		assertEquals(totalVenda, totais.stream().map(m -> m.getValorItem()).reduce(BigDecimal.ZERO, BigDecimal::add));
		assertEquals(totalCashback, totais.stream().map(m -> m.getValoCashback()).reduce(BigDecimal.ZERO, BigDecimal::add));
	}
	
}
