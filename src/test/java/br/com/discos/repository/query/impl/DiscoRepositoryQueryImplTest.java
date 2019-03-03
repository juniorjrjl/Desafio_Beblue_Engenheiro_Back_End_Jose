package br.com.discos.repository.query.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.discos.dto.DiscoDetalheDTO;
import br.com.discos.dto.DiscoListagemDTO;
import br.com.discos.model.Disco;
import br.com.discos.model.Genero;
import br.com.discos.repository.DiscoRepository;
import br.com.discos.repository.GeneroRepository;
import br.com.discos.teste.util.GeracaoDadosUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DiscoRepositoryQueryImplTest {

	@Autowired
	private DiscoRepository discoRepository;
	
	@Autowired
	private GeneroRepository generoRepository;
	
	private List<Genero> generos = new ArrayList<Genero>();
	private List<Disco> discos = new ArrayList<Disco>();
	
	@BeforeEach
	public void setUp() {
		generos = GeracaoDadosUtil.criarGeneros(3);
		generoRepository.saveAll(generos);
		discos = GeracaoDadosUtil.criarDiscos(50, generos);
		discoRepository.saveAll(discos);
	}
	
	@AfterEach
	public void tearDown() {
		discoRepository.deleteAll();
		generoRepository.deleteAll();
	}
	
	@Test
	public void quandoBuscarDiscos_RetornarPaginadoOrdenado() throws Exception {
		int pagina = 0;
		int tamanho = 10;
		Page<DiscoListagemDTO> dto = discoRepository.listar(PageRequest.of(pagina, tamanho), generos.get(1).getCodigo());
		assertEquals(tamanho, dto.getSize());
		dto.getContent().forEach(d ->{
			Disco discoRecuperado = discos.stream().filter(l -> l.getCodigo() == d.getCodigo()).findFirst().get();
			assertEquals(generos.get(1).getCodigo() , discoRecuperado.getGenero().getCodigo());
			assertEquals(discoRecuperado.getNome(), d.getNome());
			assertEquals(discoRecuperado.getPreco(), d.getPreco());
		});
		List<DiscoListagemDTO> listaComparacao = dto.getContent().stream().sorted(Comparator.comparing(DiscoListagemDTO::getNome)).collect(Collectors.toList());
		assertEquals(listaComparacao, dto.getContent());
	}
	
	@Test
	public void quandoObjetoPaginacaoNulo_DispararNullPointerException() {
		assertThrows(NullPointerException.class, () -> discoRepository.listar(null, 0));
	}
	
	@Test
	public void quandoInformarCodigoGeneroInexistente_RetornarSemFiltrarGenero() throws Exception {
		int pagina = 0;
		int tamanho = 10;
		Page<DiscoListagemDTO> dto = discoRepository.listar(PageRequest.of(pagina, tamanho), 0);
		assertEquals(tamanho, dto.getSize());
		dto.getContent().forEach(d ->{
			Disco discoRecuperado = discos.stream().filter(l -> l.getCodigo() == d.getCodigo()).findFirst().get();
			assertTrue(discoRecuperado.getGenero().getCodigo() >= 1 || discoRecuperado.getGenero().getCodigo() <= 3);
		});
		List<DiscoListagemDTO> listaComparacao = dto.getContent().stream().sorted(Comparator.comparing(DiscoListagemDTO::getNome)).collect(Collectors.toList());
		assertEquals(listaComparacao, dto.getContent());
	}
	
	@Test
	public void quandoInformarCodigoDiscoExistente_RetornarDisco() throws Exception {
		Disco discoBase = discos.get(2);
		DiscoDetalheDTO dto = discoRepository.buscarPorCodigo(discoBase.getCodigo());
		assertEquals(discoBase.getCodigo(), dto.getCodigo());
		assertEquals(discoBase.getNome(), dto.getNome());
		assertEquals(discoBase.getPreco(), dto.getPreco());
		assertEquals(generos.stream().filter(g -> g.getCodigo() == discoBase.getGenero().getCodigo()).findFirst().get().getNome(), dto.getGenero());
	}
	
	@Test
	public void quandoInformarCodigoInexistente_RetornarNulo() throws Exception {
		assertNull(discoRepository.buscarPorCodigo(discos.get(discos.size() - 1).getCodigo() + 1));
	}
	
}
