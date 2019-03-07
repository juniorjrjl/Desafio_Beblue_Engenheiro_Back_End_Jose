package br.com.discos.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.discos.dto.ItemVendaDetalheDTO;
import br.com.discos.dto.VendaDetalheDTO;
import br.com.discos.dto.VendaListagemDTO;
import br.com.discos.exceptionhandler.VendasExceptionHandler;
import br.com.discos.model.DiaSemanaEnum;
import br.com.discos.repository.query.filtro.FiltroListagemVenda;
import br.com.discos.resolver.PeriodoVendaResolver;
import br.com.discos.service.VendaService;

@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
@SpringBootTest(webEnvironment  = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VendaResourceTest {

	@Mock
	private VendaService vendaService;
	
	@InjectMocks
	private VendaResource vendaResource;
	
	private MockMvc mockMvc;

	private VendaDetalheDTO retornoVendaDetalhe = new VendaDetalheDTO(1l, LocalDate.of(2019, Month.MARCH, 20));
	private ItemVendaDetalheDTO itemVenda = new ItemVendaDetalheDTO(1l, 1, new BigDecimal(100).setScale(2, RoundingMode.HALF_DOWN), new BigDecimal(10).setScale(2, RoundingMode.HALF_DOWN), 1, "disco 1", 1, DiaSemanaEnum.SEXTA);
	private VendaDetalheDTO vendaDTO = new VendaDetalheDTO(1l, LocalDate.of(2019, 01, 01));
	
	
	private Page<VendaListagemDTO> retornoVendaLista = new PageImpl<VendaListagemDTO>(Stream.of(
			new VendaListagemDTO(1l, LocalDate.of(2019, Month.MARCH, 20)),
			new VendaListagemDTO(2l, LocalDate.of(2019, Month.APRIL, 01))
			).collect(Collectors.toList()));
	
	private ExceptionHandlerExceptionResolver createExceptionResolver() {
	    ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
	        protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
	            Method method = new ExceptionHandlerMethodResolver(VendasExceptionHandler.class).resolveMethod(exception);
	            return new ServletInvocableHandlerMethod(new VendasExceptionHandler(), method);
	        }
	    };
	    exceptionResolver.afterPropertiesSet();
	    return exceptionResolver;
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mockMvc = MockMvcBuilders.standaloneSetup(vendaResource)
        		.setControllerAdvice(new VendasExceptionHandler())
        		.setHandlerExceptionResolvers(createExceptionResolver())
        		.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(), new PeriodoVendaResolver())
        		.setMessageConverters(mappingJackson2HttpMessageConverter)
        		.build();
        
        
        retornoVendaDetalhe.setValorTotal(new BigDecimal(100).setScale(2, RoundingMode.HALF_DOWN));
        retornoVendaDetalhe.setValorCashback(new BigDecimal(10).setScale(2, RoundingMode.HALF_DOWN));
        retornoVendaDetalhe.getItensVenda().add(itemVenda);
		when(vendaService.buscarPorCodigo(1l)).thenReturn(retornoVendaDetalhe);
		when(vendaService.buscarPorCodigo(404l)).thenReturn(null);
		when(vendaService.buscarPorCodigo(500l)).thenThrow(Exception.class);
		
		retornoVendaLista.getContent().get(0).setValorTotal(new BigDecimal(100).setScale(2, RoundingMode.HALF_DOWN));
		retornoVendaLista.getContent().get(0).setValorCashback(new BigDecimal(10).setScale(2, RoundingMode.HALF_DOWN));
		retornoVendaLista.getContent().get(1).setValorTotal(new BigDecimal(200).setScale(2, RoundingMode.HALF_DOWN));
		retornoVendaLista.getContent().get(1).setValorCashback(new BigDecimal(20).setScale(2, RoundingMode.HALF_DOWN));
		when(vendaService.listar(Mockito.argThat(a -> a != null && a.getPageNumber() == 1 && a.getPageSize() == 2), Mockito.argThat(a -> a != null))).thenReturn(retornoVendaLista);
		when(vendaService.listar(Mockito.argThat(a -> a != null && a.getPageNumber() == 404 && a.getPageSize() == 404), Mockito.argThat(a -> a != null))).thenReturn(new PageImpl<VendaListagemDTO>(new ArrayList<VendaListagemDTO>()));
		when(vendaService.listar(Mockito.argThat(a -> a != null && a.getPageNumber() == 500 && a.getPageSize() == 500), Mockito.argThat(a -> a != null))).thenThrow(Exception.class);
		
		vendaDTO.setValorCashback(new BigDecimal(10));
		vendaDTO.setValorTotal(new BigDecimal(100));
		vendaDTO.getItensVenda().add(new ItemVendaDetalheDTO(1l, 1, new BigDecimal(100), new BigDecimal(10), 1, "disco 1", 1, DiaSemanaEnum.SEGUNDA));
		when(vendaService.cadastrar(Mockito.argThat(a -> a != null && a.getItensVenda().size() == 1))).thenThrow(Exception.class);
		when(vendaService.cadastrar(Mockito.argThat(a -> a != null && a.getItensVenda().size() != 1))).thenReturn(vendaDTO);
	}
	
	@Test
	public void verificandoRetornoDiscoDetalhes() throws Exception {
		String jsonEsperado = "{\"codigo\":%s,\"data\":\"%s\",\"valorTotal\":%s,\"valorCashback\":%s,"
				               + "\"itensVenda\":[{\"codigo\":%s,\"quantidade\":%s,\"valorUnitario\":%s,"
				               + "\"porcentagemCashback\":%s,\"codigoDisco\":%s,\"nomeDisco\":\"%s\","
				               + "\"codigoCashback\":%s,\"dia\":\"%s\",\"valorTotal\":%s,\"valorCashback\":%s}]}";
		MvcResult result = mockMvc.perform(get("/vendas/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		assertEquals(String.format(jsonEsperado, retornoVendaDetalhe.getCodigo(), 
				retornoVendaDetalhe.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 
				retornoVendaDetalhe.getValorTotal().toString(), 
				retornoVendaDetalhe.getValorCashback().toString(),
				itemVenda.getCodigo(),
				itemVenda.getQuantidade(),
				itemVenda.getValorUnitario().toString(),
				itemVenda.getPorcentagemCashback().toString(),
				itemVenda.getCodigoDisco(),
				itemVenda.getNomeDisco(),
				itemVenda.getCodigoCashback(),
				itemVenda.getDia(),
				itemVenda.getValorTotal(),
				itemVenda.getValorCashback()),
				result.getResponse().getContentAsString());
	}
	
	@Test
	public void quandoBuscarDiscoInexistente_Retornar404() throws Exception {
		mockMvc.perform(get("/vendas/404")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();
	}
	
	@Test
	public void quandoDetalhesDispararException_Retornar500() throws Exception {
		mockMvc.perform(get("/vendas")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andReturn();
	}
	
	static Stream<FiltroListagemVenda> parametrosGetVendas(){
		return Stream.of(
				new FiltroListagemVenda(),
				new FiltroListagemVenda(null, LocalDate.now()),
				new FiltroListagemVenda(LocalDate.now(), null),
				new FiltroListagemVenda(LocalDate.now(), LocalDate.now()));
	}
	
	@DisplayName("verificando retorno lista vendas")
	@ParameterizedTest
	@MethodSource("parametrosGetVendas")
	public void verificandoRetornoListaVendas(FiltroListagemVenda filtro) throws Exception {
		List<VendaListagemDTO> dto = retornoVendaLista.getContent();
		MvcResult result = mockMvc.perform(get("/vendas")
				.param("page", "1")
				.param("size", "2")
				.param("dataInicial", filtro.getDataInicial() != null ? filtro.getDataInicial().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
				.param("dataFinal", filtro.getDataFinal() != null ? filtro.getDataFinal().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		String jsonRetorno = result.getResponse().getContentAsString();
		assertTrue(jsonRetorno.contains(String.format("\"codigo\":%s", dto.get(0).getCodigo())));
		assertTrue(jsonRetorno.contains(String.format("\"codigo\":%s", dto.get(1).getCodigo())));
		assertTrue(jsonRetorno.contains(String.format("\"data\":\"%s\"", dto.get(0).getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))));
		assertTrue(jsonRetorno.contains(String.format("\"data\":\"%s\"", dto.get(1).getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))));
		assertTrue(jsonRetorno.contains(String.format("\"valorTotal\":%s", dto.get(0).getValorTotal().toString())));
		assertTrue(jsonRetorno.contains(String.format("\"valorTotal\":%s", dto.get(1).getValorTotal().toString())));
		assertTrue(jsonRetorno.contains(String.format("\"valorCashback\":%s", dto.get(0).getValorCashback().toString())));
		assertTrue(jsonRetorno.contains(String.format("\"valorCashback\":%s", dto.get(1).getValorCashback().toString())));
	}
	
	@DisplayName("verificando httpstatus 404")
	@ParameterizedTest
	@MethodSource("parametrosGetVendas")
	public void quandoBuscarVendasInexistente_Retornar404(FiltroListagemVenda filtro) throws Exception {
		mockMvc.perform(get("/vendas")
				.param("page", "404")
				.param("size", "404")
				.param("dataInicial", filtro.getDataInicial() != null ? filtro.getDataInicial().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
				.param("dataFinal", filtro.getDataFinal() != null ? filtro.getDataFinal().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();
	}
	
	@DisplayName("verificando httpstatus 500")
	@ParameterizedTest
	@MethodSource("parametrosGetVendas")
	public void quandoBendasDispararException_Retornar500(FiltroListagemVenda filtro) throws Exception {
		mockMvc.perform(get("/vendas")
				.param("page", "500")
				.param("size", "500")
				.param("dataInicial", filtro.getDataInicial() != null ? filtro.getDataInicial().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
				.param("dataFinal", filtro.getDataFinal() != null ? filtro.getDataFinal().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andReturn();
	}
	
	
	private final String JSON_VENDA = "{\"data\":\"%s\"";
	private final String JSON_ITEM_VENDA = "{\"quantidade\":%s,\"codigoDisco\":%s}";
	
	/*faltou configuração para implementar devidamente estes testes
	static Stream<Arguments> parametrosPostVenda(){
		return Stream.of(
					Arguments.of(null, "1", "1", "1", "1", "Data é obrigatório"),
					Arguments.of("2019-01-01", null, "1", "1", "1", "Quantidade deve ter o mínimo de 1"),
					Arguments.of("2019-01-01", "1", null, "1", "1", "Informe um disco válido para o item da venda"),
					Arguments.of("2019-01-01", "1", "1", null, "1", "Quantidade deve ter o mínimo de 1"),
					Arguments.of("2019-01-01", "1", "1", "1", null, "Informe um disco válido para o item da venda")
				);
	}
	
	@DisplayName("verificando validações de campos")
	@ParameterizedTest
	@MethodSource("parametrosPostVenda")
	public void quandoInformarVendaInvalida_Retornar404(String data, String quantidadeItem1, String codigoDiscoItem1, String quantidadeItem2, String codigoDiscoItem2, String mensagemErro) throws Exception {
		String jsonVenda = String.format(JSON_VENDA, data);
		String jsonIten1 = String.format(JSON_ITEM_VENDA, quantidadeItem1, codigoDiscoItem1);
		String jsonIten2 = String.format(JSON_ITEM_VENDA, quantidadeItem2, codigoDiscoItem2);
		String json = String.format("%s,\"itensVenda\":[%s,%s]}", jsonVenda, jsonIten1, jsonIten2);
		mockMvc.perform(post("/vendas")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json))
				.andDo(r ->{
					assertTrue(r.getResponse().getContentAsString().contains(mensagemErro));
				})
		.andExpect(status().isBadRequest())
		.andReturn();
	}

	@Test
	public void quandoInformarVendaSemItem_Retornar404() throws Exception {
		mockMvc.perform(post("/vendas")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(String.format(JSON_VENDA + "}", "2019-01-01")))
				.andDo(r -> {
					System.out.println(r.getResponse().getContentAsString());
					assertTrue(r.getResponse().getContentAsString().contains("A venda deve ter pelo menos 1 item")); 
				})
		.andExpect(status().isBadRequest())
		.andReturn();
	}*/
	
	@Test
	public void quandoCadastroVendaDispararException_Retornar500() throws Exception {
		String jsonVenda = String.format(JSON_VENDA, "2019-01-01");
		String jsonIten1 = String.format(JSON_ITEM_VENDA, "1", "1");
		String json = String.format("%s,\"itensVenda\":[%s]}", jsonVenda, jsonIten1);
		mockMvc.perform(post("/vendas")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json))
				.andDo(print())
		.andExpect(status().isInternalServerError())
		.andReturn();
	}
	
	@Test
	public void verificandoRetornoCadastroVenda() throws Exception {
		String jsonVenda = String.format(JSON_VENDA, "2019-01-02");
		String jsonIten1 = String.format(JSON_ITEM_VENDA, "1", "1");
		String jsonIten2 = String.format(JSON_ITEM_VENDA, "2", "2");
		String json = String.format("%s,\"itensVenda\":[%s,%s]}", jsonVenda, jsonIten1, jsonIten2);
		mockMvc.perform(post("/vendas")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json))
				.andDo(r ->{
					String retorno = r.getResponse().getContentAsString();
					assertTrue(retorno.contains(String.valueOf(vendaDTO.getCodigo())));
					assertTrue(retorno.contains(vendaDTO.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
					assertTrue(retorno.contains(vendaDTO.getValorCashback().toString()));
					assertTrue(retorno.contains(vendaDTO.getValorTotal().toString()));
					assertTrue(retorno.contains(vendaDTO.getItensVenda().get(0).getNomeDisco()));
					
					assertTrue(retorno.contains(String.valueOf(vendaDTO.getItensVenda().get(0).getQuantidade())));
					assertTrue(retorno.contains(vendaDTO.getItensVenda().get(0).getValorUnitario().toString()));
					assertTrue(retorno.contains(vendaDTO.getItensVenda().get(0).getPorcentagemCashback().toString()));
					assertTrue(retorno.contains(String.valueOf(vendaDTO.getItensVenda().get(0).getCodigoDisco())));
					assertTrue(retorno.contains(String.valueOf(vendaDTO.getItensVenda().get(0).getCodigoCashback())));
					assertTrue(retorno.contains(vendaDTO.getItensVenda().get(0).getDia().toString()));
					assertTrue(retorno.contains(String.valueOf(vendaDTO.getItensVenda().get(0).getValorTotal())));
					assertTrue(retorno.contains(String.valueOf(vendaDTO.getItensVenda().get(0).getValorCashback())));
					
				})
		.andExpect(status().isCreated());
	}
	
}
