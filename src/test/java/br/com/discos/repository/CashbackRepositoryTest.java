package br.com.discos.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.discos.model.Cashback;
import br.com.discos.model.DiaSemanaEnum;
import br.com.discos.model.Genero;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CashbackRepositoryTest {
	
	@Autowired
	private GeneroRepository generoRepository;
	
	@Autowired
	private CashbackRepository cashbackRepository;
	
	private Cashback cashback = new Cashback();
	
	private Genero genero = new Genero();
	
	@BeforeEach
	public void setUp() {
		genero.setNome("genero cashback");
		generoRepository.save(genero);
		
		cashback.setDia(DiaSemanaEnum.DOMINGO);
		cashback.setGenero(genero);
		cashback.setPorcentagem(new BigDecimal(35.00));
		cashbackRepository.save(cashback);
	}
	
	@AfterEach
	public void tearDown() {
		cashbackRepository.deleteAll();
		generoRepository.deleteAll();
	}
	
	@Test
	public void quandoExcluirCashback_ManterGenero() {
		assertDoesNotThrow(() -> cashbackRepository.deleteById(cashback.getCodigo()));
		assertNotNull(generoRepository.findById(genero.getCodigo()).get());
	}
	
	static Stream<Arguments> parametros(){
		return Stream.of(
					Arguments.of(DiaSemanaEnum.DOMINGO, 1),
					Arguments.of(DiaSemanaEnum.SEGUNDA, 0),
					Arguments.of(DiaSemanaEnum.SEGUNDA, 1)
				);
	}
	
	@DisplayName("esperando não receber resultados")
	@ParameterizedTest
	@MethodSource("parametros")
	public void QuandoNaoEncontrar_RetornarNulo(DiaSemanaEnum dia, int incrementarCodigo) {
		assertNull(cashbackRepository.buscarInformacoesCashbackDia(genero.getCodigo() + 1, dia));
	}
	
	
	
}
