package br.com.discos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = {"itensVenda"})
@NoArgsConstructor
@Getter @Setter
public class VendaDetalheDTO {

	private long codigo;
	private LocalDate data;
	private BigDecimal valorTotal;
	private BigDecimal valorCashback;
	private List<ItemVendaDetalheDTO> itensVenda = new ArrayList<ItemVendaDetalheDTO>();
	
	
	public VendaDetalheDTO(long codigo, LocalDate data) {
		super();
		this.codigo = codigo;
		this.data = data;
	}
	
}
