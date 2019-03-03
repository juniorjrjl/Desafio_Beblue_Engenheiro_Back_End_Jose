package br.com.discos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.discos.model.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long> {

}
