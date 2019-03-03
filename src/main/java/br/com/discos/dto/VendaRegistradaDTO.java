package br.com.discos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class VendaRegistradaDTO {

	public long codigo;
	public LocalDateTime dataHora;
	private List<ItemVendaRegistradaDTO> itensVenda = new ArrayList<ItemVendaRegistradaDTO>();
	private BigDecimal totalVenda;
	private BigDecimal totalCashback;
	
}
