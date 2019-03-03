package br.com.discos.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.discos.model.Cashback;
import br.com.discos.model.Disco;
import br.com.discos.model.Genero;
import br.com.discos.model.ItemVenda;
import br.com.discos.model.Venda;
import br.com.discos.teste.util.GeracaoDadosUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ItemVendaRepositoryTest {

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
		
		vendas = GeracaoDadosUtil.criarVendas(1, discos, cashbacks);
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
	public void quandoExcluirItemVenda_ManterVendaDiscoCashback() {
		ItemVenda itemVenda = vendas.get(0).getItensVenda().get(0);
		assertDoesNotThrow(() -> itemVendaRepository.deleteById(itemVenda.getCodigo()));
		assertNotNull(vendaRepository.findById(itemVenda.getVenda().getCodigo()));
		assertNotNull(discoRepository.findById(itemVenda.getDisco().getCodigo()));
		assertNotNull(cashbackRepository.findById(itemVenda.getCashback().getCodigo()));
	}
	
}
