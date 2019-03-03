package br.com.discos.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
	
}
