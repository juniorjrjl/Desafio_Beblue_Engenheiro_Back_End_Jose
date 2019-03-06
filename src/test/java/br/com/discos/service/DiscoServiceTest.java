package br.com.discos.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.discos.dto.DiscoCadastroDTO;
import br.com.discos.exception.DiscoServiceExcepion;

@SpringBootTest
public class DiscoServiceTest {
	
	@Autowired
	private DiscoService discoService;
	
	@Test
	public void quandoInformarObjetoNuloDispararException() {
		assertThrows(NullPointerException.class, () -> discoService.cadastrar(null));
	}
	
	static Stream<Arguments> parametros(){
		return Stream.of(
				Arguments.of(new DiscoCadastroDTO(null, new BigDecimal(1), 0l), "javax.validation.constraints.NotNull.message"),
				Arguments.of(new DiscoCadastroDTO("t", null, 0l), "javax.validation.constraints.NotNull.message"),
				Arguments.of(new DiscoCadastroDTO("    ", new BigDecimal(1), 0l), "javax.validation.constraints.NotBlank.message"),
				Arguments.of(new DiscoCadastroDTO("123456789012345678901234567890"
							+ "1234567890123456789012345678901234567890"
							+ "1234567890123456789012345678901234567890"
							+ "1234567890123456789012345678901234567890"
							+ "1234567890123456789012345678901234567890"
							+ "12345678901", new BigDecimal(1), 0), "javax.validation.constraints.Size.message"),
				Arguments.of(new DiscoCadastroDTO("t", new BigDecimal(-1), 1l), "javax.validation.constraints.Min.message")
				);
	}
	
	@DisplayName("verificando validações do dto")
	@ParameterizedTest
	@MethodSource("parametros")
	public void quandoDTOInvalido_DispararException(DiscoCadastroDTO dto, String violacao) {
		DiscoServiceExcepion ex = assertThrows(DiscoServiceExcepion.class, ()-> discoService.cadastrar(dto));
		assertTrue(ex.getMessage().contains(violacao));
	}

	
}
