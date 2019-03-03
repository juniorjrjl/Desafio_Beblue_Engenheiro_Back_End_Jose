package br.com.discos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.discos.model.ItemVenda;
import br.com.discos.repository.query.ItemVendaRepositoryQuery;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long>, ItemVendaRepositoryQuery {

}
