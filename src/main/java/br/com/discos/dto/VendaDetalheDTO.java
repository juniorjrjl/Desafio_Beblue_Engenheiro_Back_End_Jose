package br.com.discos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class VendaDetalheDTO {

	private long codigo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dataHoraVenda;
	private BigDecimal valorTotal;
	private BigDecimal valorCashback;
	private List<ItemVendaDetalheDTO> itensVenda = new ArrayList<ItemVendaDetalheDTO>();
	
	
	public VendaDetalheDTO(long codigo, LocalDateTime dataHoraVenda) {
		super();
		this.codigo = codigo;
		this.dataHoraVenda = dataHoraVenda;
	}
	
}
