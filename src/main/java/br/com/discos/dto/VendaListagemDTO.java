package br.com.discos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class VendaListagemDTO {

	private long codigo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dataHoraVenda;
	private BigDecimal valorTotal;
	private BigDecimal valorCashback;
	
	public VendaListagemDTO(long codigo, LocalDateTime dataHoraVenda) {
		super();
		this.codigo = codigo;
		this.dataHoraVenda = dataHoraVenda;
	}
	
}
