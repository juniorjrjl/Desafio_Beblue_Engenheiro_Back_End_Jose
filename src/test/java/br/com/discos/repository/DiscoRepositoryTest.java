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

import br.com.discos.model.Disco;
import br.com.discos.model.Genero;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DiscoRepositoryTest {

	@Autowired
	private DiscoRepository discoRepository;
	
	@Autowired
	private GeneroRepository generoRepository;
	
	private Genero genero = new Genero();
	
	private Disco disco = new Disco();
	
	@BeforeEach
	public void setUp() {
		genero.setNome("genero cashback");
		generoRepository.save(genero);
		
		disco.setGenero(genero);
		disco.setNome("disco 1");
		disco.setPreco(new BigDecimal(10.00));
		discoRepository.save(disco);
	}
	
	@AfterEach
	public void tearDown() {
		discoRepository.deleteAll();
		generoRepository.deleteAll();
	}
	
	@Test
	public void quandoExcluirDisco_ManterGenero() {
		assertDoesNotThrow(() -> discoRepository.deleteById(disco.getCodigo()));
		assertNotNull(generoRepository.findById(genero.getCodigo()).get());
	}
	
}
