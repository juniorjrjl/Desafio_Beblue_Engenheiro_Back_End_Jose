package br.com.discos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.discos.dto.GeneroListagemDTO;
import br.com.discos.model.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long> {

	@Query("select new br.com.discos.dto.GeneroListagemDTO(g.codigo, g.nome) from generos g")
	public List<GeneroListagemDTO> buscarNomesCodigos();
	
}
