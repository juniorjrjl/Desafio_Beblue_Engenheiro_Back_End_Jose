package br.com.discos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.discos.dto.CashbackVendaDTO;
import br.com.discos.model.Cashback;
import br.com.discos.model.DiaSemanaEnum;

public interface CashbackRepository extends JpaRepository<Cashback, Long> {

	@Query("select new br.com.discos.dto.CashbackVendaDTO(c.codigo, c.porcentagem) from cashbacks c where c.genero.codigo = ?1 and c.dia = ?2")
	public CashbackVendaDTO buscarInformacoesCashbackDia(long codigoGenero, DiaSemanaEnum dia);
	
}
