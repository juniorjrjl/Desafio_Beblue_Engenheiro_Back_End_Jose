package br.com.discos.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.discos.dto.DiscoDetalheDTO;
import br.com.discos.dto.DiscoListagemDTO;
import br.com.discos.service.DiscoService;

@SpringBootTest
public class DiscoResourceTest {

	@Mock
	private DiscoService discoService;
	
	@InjectMocks
	private DiscoResource discoResource;
	
	private DiscoDetalheDTO retornoDiscoDetalhe = new DiscoDetalheDTO(1l, "disco 1", new BigDecimal(20.00).setScale(2, BigDecimal.ROUND_HALF_UP), "Rock");
	
	private MockMvc mockMvc;
	
	private Page<DiscoListagemDTO> retornoListaDiscos = new PageImpl<DiscoListagemDTO>(Stream.of(
			new DiscoListagemDTO(1l, "disco 1", new BigDecimal(20).setScale(2, BigDecimal.ROUND_HALF_UP)),
			new DiscoListagemDTO(1l, "disco 2", new BigDecimal(19.90).setScale(2, BigDecimal.ROUND_HALF_UP))
			).collect(Collectors.toList()));
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(discoResource)
        		.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        		.build();
		when(discoService.buscarPorCodigo(1l)).thenReturn(retornoDiscoDetalhe);
		when(discoService.buscarPorCodigo(404l)).thenReturn(null);
		when(discoService.buscarPorCodigo(500l)).thenThrow(Exception.class);
		when(discoService.listar(PageRequest.of(1, 2), 1l)).thenReturn(retornoListaDiscos);
		when(discoService.listar(PageRequest.of(404, 404), 404l)).thenReturn(new PageImpl<DiscoListagemDTO>(new ArrayList<DiscoListagemDTO>()));
		when(discoService.listar(PageRequest.of(500, 500), 500l)).thenThrow(Exception.class);
	}
	
	@Test
	public void verificandoRetornoDiscoDetalhes() throws Exception {
		MvcResult result = mockMvc.perform(get("/discos/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		assertEquals(String.format("{\"codigo\":%s,\"nome\":\"%s\",\"preco\":%s,\"genero\":\"%s\"}", retornoDiscoDetalhe.getCodigo(), retornoDiscoDetalhe.getNome(), retornoDiscoDetalhe.getPreco().toString(), retornoDiscoDetalhe.getGenero()), 
				result.getResponse().getContentAsString());
	}
	
	@Test
	public void quandoBuscarDiscoInexistente_Retornar404() throws Exception {
		mockMvc.perform(get("/discos/404")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();
	}
	
	@Test
	public void quandoDetalhesDispararException_Retornar500() throws Exception {
		mockMvc.perform(get("/discos/500")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andReturn();
	}
	
	@Test
	public void verificandoRetornorListaDiscos() throws Exception {
		MvcResult result = mockMvc.perform(get("/discos/")
				.param("page", "1")
				.param("size", "2")
				.param("codigoGenero", "1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("{\"codigo\":1,\"nome\":\"disco 1\",\"preco\":20.00}"));
		assertTrue(result.getResponse().getContentAsString().contains("{\"codigo\":1,\"nome\":\"disco 2\",\"preco\":19.90}],"));
	}
	
	@Test
	public void quandoBuscarPaginaInexistente_Retornar404() throws Exception {
		mockMvc.perform(get("/discos/")
				.param("page", "404")
				.param("size", "404")
				.param("codigoGenero", "404")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();
	}
	
	@Test
	public void quandoPaginacaoDispararException_Retornar500() throws Exception {
		mockMvc.perform(get("/discos/")
				.param("page", "500")
				.param("size", "500")
				.param("codigoGenero", "500")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andReturn();
	}
	
}
