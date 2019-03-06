package br.com.discos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter @Setter
public class VendaListagemDTO {

	private long codigo;
	private LocalDate data;
	private BigDecimal valorTotal;
	private BigDecimal valorCashback;
	
	public VendaListagemDTO(long codigo, LocalDate data) {
		super();
		this.codigo = codigo;
		this.data = data;
	}
	
}
