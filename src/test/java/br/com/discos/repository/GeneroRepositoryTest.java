package br.com.discos.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.discos.dto.GeneroListagemDTO;
import br.com.discos.model.Cashback;
import br.com.discos.model.DiaSemanaEnum;
import br.com.discos.model.Disco;
import br.com.discos.model.Genero;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GeneroRepositoryTest{
	
	@Autowired
	private DiscoRepository discoRepository;
	
	@Autowired
	private GeneroRepository generoRepository;
	
	@Autowired
	private CashbackRepository cashbackRepository;
	
	private Genero generoDisco = new Genero();
	
	private Genero generoCashback = new Genero();
	
	@BeforeEach
	public void setUp() {
		generoCashback.setNome("genero cashback");
		generoDisco.setNome("genero disco");
		generoRepository.save(generoDisco);
		generoRepository.save(generoCashback);
		
		Disco disco = new Disco();
		disco.setGenero(generoDisco);
		disco.setNome("disco 1");
		disco.setPreco(new BigDecimal(10.00));
		discoRepository.save(disco);
		
		Cashback cashback = new Cashback();
		cashback.setDia(DiaSemanaEnum.DOMINGO);
		cashback.setGenero(generoCashback);
		cashback.setPorcentagem(new BigDecimal(35.00));
		cashbackRepository.save(cashback);
	}
	
	@AfterEach
	public void tearDown() {
		discoRepository.deleteAll();
		cashbackRepository.deleteAll();
		generoRepository.deleteAll();
	}
	
	@Test
	public void QuandoExcluirGeneroComDisco_DispararException() {
		assertThrows(Exception.class, () -> generoRepository.deleteAll());
	}
	
	@Test
	public void QuandoExcluirGeneroComCashback_DispararException() {
		assertThrows(Exception.class, () -> generoRepository.deleteAll());
	}
	
	@Test
	public void buscandoListaDiscos() {
		List<GeneroListagemDTO> dto = generoRepository.buscarNomesCodigos();
		assertFalse(dto.isEmpty());
	}
	
}
