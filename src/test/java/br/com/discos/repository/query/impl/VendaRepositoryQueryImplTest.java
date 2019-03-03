package br.com.discos.repository.query.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.discos.dto.VendaDetalheDTO;
import br.com.discos.dto.VendaListagemDTO;
import br.com.discos.model.Cashback;
import br.com.discos.model.Disco;
import br.com.discos.model.Genero;
import br.com.discos.model.Venda;
import br.com.discos.repository.CashbackRepository;
import br.com.discos.repository.DiscoRepository;
import br.com.discos.repository.GeneroRepository;
import br.com.discos.repository.VendaRepository;
import br.com.discos.repository.query.filtro.FiltroListagemVenda;
import br.com.discos.teste.util.GeracaoDadosUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class VendaRepositoryQueryImplTest {

	@Autowired
	private DiscoRepository discoRepository;
	
	@Autowired
	private GeneroRepository generoRepository;
	
	@Autowired
	private VendaRepository vendaRepository;
	
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
		
		vendas = GeracaoDadosUtil.criarVendas(50, discos, cashbacks);
		vendaRepository.saveAll(vendas);
	}
	
	@AfterEach
	public void tearDown() {
		vendaRepository.deleteAll();
		discoRepository.deleteAll();
		cashbackRepository.deleteAll();
		generoRepository.deleteAll();
	}

	static Stream<FiltroListagemVenda> filtroQuery(){
		return Stream.of(
					new FiltroListagemVenda(LocalDateTime.of(2019, 1, 13, 0, 0, 0), LocalDateTime.of(2019, 1, 25, 0, 0, 0)), 
					new FiltroListagemVenda(LocalDateTime.of(2019, 1, 13, 0, 0, 0), null), 
					new FiltroListagemVenda(null, LocalDateTime.of(2019, 1, 25, 0, 0, 0)),
					new FiltroListagemVenda()
				);
	}
	
	@DisplayName("busncado vendas usando filtro")
	@ParameterizedTest
	@MethodSource("filtroQuery")
	public void buscandoVendasPaginadas(FiltroListagemVenda filtro) throws Exception {
		int pagina = 0;
		int tamanho = 10;
		Page<VendaListagemDTO> dto = vendaRepository.listar(PageRequest.of(pagina, tamanho), filtro);
		assertEquals(tamanho, dto.getSize());
		dto.forEach(d -> {
			if (filtro.getDataInicial() != null) {
				assertTrue(d.getDataHoraVenda().isAfter(filtro.getDataInicial()) || d.getDataHoraVenda().isEqual(filtro.getDataInicial()));
			}
			if (filtro.getDataFinal() != null) {
				assertTrue(d.getDataHoraVenda().isBefore(filtro.getDataFinal()) || d.getDataHoraVenda().isEqual(filtro.getDataFinal()));
			}
		});
		List<VendaListagemDTO> listaComparacao = dto.getContent().stream().sorted(Comparator.comparing(VendaListagemDTO::getDataHoraVenda).reversed()).collect(Collectors.toList());
		assertEquals(listaComparacao, dto.getContent());
	}
	
	@Test
	public void quandoBuscarVendaExistente_Retornar() throws Exception {
		Venda vendaBaseTeste = vendas.get(0);
		VendaDetalheDTO dto = vendaRepository.buscarPorCodigo(vendaBaseTeste.getCodigo());
		assertEquals(vendaBaseTeste.getDataHoraVenda(), dto.getDataHoraVenda());
	}
	
	@Test
	public void quandoBuscarVendaInexistente_RetornarNulo() throws Exception {
		assertNull(vendaRepository.buscarPorCodigo(vendas.get(vendas.size() -1).getCodigo() + 1));
	}
	
}
