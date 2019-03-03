package br.com.discos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.discos.model.Cashback;

public interface CashbackRepository extends JpaRepository<Cashback, Long> {

}
