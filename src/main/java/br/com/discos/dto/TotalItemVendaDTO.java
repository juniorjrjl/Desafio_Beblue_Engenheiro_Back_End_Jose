package br.com.discos.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class TotalItemVendaDTO {

	private BigDecimal valorItem;
	private BigDecimal valoCashback;
	
	public TotalItemVendaDTO(BigDecimal valorItem, BigDecimal percentualCashback, long quantidade) {
		this.valorItem = valorItem.multiply(new BigDecimal(quantidade)).setScale(2, RoundingMode.HALF_DOWN);
		this.valoCashback = this.valorItem.multiply(percentualCashback).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN);
	}
	
}
