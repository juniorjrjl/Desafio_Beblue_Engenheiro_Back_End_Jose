package br.com.discos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.discos.model.Venda;
import br.com.discos.repository.query.VendaRepositoryQuery;

public interface VendaRepository extends JpaRepository<Venda, Long>, VendaRepositoryQuery {

}
