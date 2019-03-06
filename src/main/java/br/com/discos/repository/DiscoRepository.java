package br.com.discos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.discos.dto.DiscoPrecoGeneroDTO;
import br.com.discos.model.Disco;
import br.com.discos.repository.query.DiscoRepositoryQuery;

public interface DiscoRepository extends JpaRepository<Disco, Long>, DiscoRepositoryQuery {

	@Query("select new br.com.discos.dto.DiscoPrecoGeneroDTO(d.preco, d.genero.codigo) from discos d where d.codigo = ?1")
	public DiscoPrecoGeneroDTO buscarValorDisco(long codigo);
	
}
