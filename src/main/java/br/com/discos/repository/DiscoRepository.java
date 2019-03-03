package br.com.discos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.discos.model.Disco;
import br.com.discos.repository.query.DiscoRepositoryQuery;

public interface DiscoRepository extends JpaRepository<Disco, Long>, DiscoRepositoryQuery {

}
