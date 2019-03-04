package br.com.discos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.discos.dto.ItemVendaDetalheDTO;
import br.com.discos.dto.TotalItemVendaDTO;
import br.com.discos.dto.VendaDetalheDTO;
import br.com.discos.dto.VendaListagemDTO;
import br.com.discos.model.DiaSemanaEnum;
import br.com.discos.repository.ItemVendaRepository;
import br.com.discos.repository.VendaRepository;
import br.com.discos.repository.query.filtro.FiltroListagemVenda;

@SpringBootTest
public class VendaServiceTest {

	@Mock
	private VendaRepository vendaRepository;
	
	@Mock
	private ItemVendaRepository itemVendaRepository;
	
	@InjectMocks
	private VendaService vendaService;
	
	private List<TotalItemVendaDTO> retornoTotails = Stream.of(
			new TotalItemVendaDTO(new BigDecimal(49.89), new BigDecimal(7.3), 5l),
			new TotalItemVendaDTO(new BigDecimal(19.71), new BigDecimal(1.06), 10l),
			new TotalItemVendaDTO(new BigDecimal(9.56), new BigDecimal(0.05), 20l)
			).collect(Collectors.toList());
	
	@BeforeEach
	public void setUp() throws Exception {
		when(vendaRepository.buscarPorCodigo(1l)).thenReturn(new VendaDetalheDTO(1l, LocalDateTime.now()));
		when(itemVendaRepository.buscarTotaisItemVenda(Mockito.longThat(a -> a != 2l))).thenReturn(retornoTotails);
		when(itemVendaRepository.listarItensVendaPorCodigoVenda(Mockito.longThat(a -> a != 2l))).thenReturn(Stream.of(
				new ItemVendaDetalheDTO(1, 5l, new BigDecimal(49.89), new BigDecimal(7.3), 1l, "disco 1", 1l, DiaSemanaEnum.DOMINGO),
				new ItemVendaDetalheDTO(1, 10l, new BigDecimal(19.71), new BigDecimal(1.06), 2l, "disco 2", 2l, DiaSemanaEnum.DOMINGO),
				new ItemVendaDetalheDTO(1, 20l, new BigDecimal(9.56), new BigDecimal(0.05), 3l, "disco 3", 3l, DiaSemanaEnum.DOMINGO)
				).collect(Collectors.toList()));
		
		when(vendaRepository.buscarPorCodigo(2l)).thenReturn(new VendaDetalheDTO(2l, LocalDateTime.now()));
		when(itemVendaRepository.buscarTotaisItemVenda(2l)).thenReturn(null);
		when(itemVendaRepository.listarItensVendaPorCodigoVenda(2l)).thenReturn(null);
		
		when(vendaRepository.listar(Mockito.any(Pageable.class), Mockito.any(FiltroListagemVenda.class)))
			.thenReturn(new PageImpl<VendaListagemDTO>(Stream.of(
					new VendaListagemDTO(3l, LocalDateTime.now()),
					new VendaListagemDTO(4l, LocalDateTime.now())
					).collect(Collectors.toList())));
	}
	
	@Test
	public void verificarCalculoTotalizadorVenda() throws Exception {
		VendaDetalheDTO dto = vendaService.buscarPorCodigo(1l);
		BigDecimal valorTotal = retornoTotails.stream().map(m -> m.getValorItem()).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal valorCashback = retornoTotails.stream().map(m -> m.getValoCashback()).reduce(BigDecimal.ZERO, BigDecimal::add);
		assertFalse(dto.getItensVenda().isEmpty());
		assertEquals(valorTotal, dto.getValorTotal());
		assertEquals(valorCashback, dto.getValorCashback());
	}
	
	@Test
	public void quandoNaoTiverItens_RetornarTotaisNulos() throws Exception {
		VendaDetalheDTO dto = vendaService.buscarPorCodigo(2l);
		assertNull(dto.getValorTotal());
		assertNull(dto.getValorCashback());
	}
	
	static Stream<Arguments> parametros(){
		return Stream.of(
					Arguments.of(null, null),
					Arguments.of(null, new FiltroListagemVenda() ),
					Arguments.of(PageRequest.of(0, 2), null)
				);
	}
	
	@DisplayName("verificando disparo de NullPointerException")
	@ParameterizedTest
	@MethodSource("parametros")
	public void quandoParametroNulo_DispararException(Pageable pagable, FiltroListagemVenda filtro) {
		assertThrows(NullPointerException.class, () -> vendaService.listar(pagable, filtro));
	}
	
	@Test
	public void verificarListagemVendas() throws Exception {
		Page<VendaListagemDTO> dto = vendaService.listar(PageRequest.of(0, 2), new FiltroListagemVenda());
		dto.getContent().forEach(p -> {
			BigDecimal valorTotal = retornoTotails.stream().map(m -> m.getValorItem()).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal valorCashback = retornoTotails.stream().map(m -> m.getValoCashback()).reduce(BigDecimal.ZERO, BigDecimal::add);
			assertEquals(valorTotal, p.getValorTotal());
			assertEquals(valorCashback, p.getValorCashback());
		});
	}
	
}
