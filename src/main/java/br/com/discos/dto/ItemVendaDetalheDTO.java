package br.com.discos.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.discos.model.DiaSemanaEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class ItemVendaDetalheDTO {

	private long codigo;
	private long quantidade;
	private BigDecimal valorUnitario;
	private BigDecimal porcentagemCashback;
	private long codigoDisco;
	private String nomeDisco;
	private long codigoCashback;
	private DiaSemanaEnum dia;
	private BigDecimal valorTotal;
	private BigDecimal valorCashback;
	
	public ItemVendaDetalheDTO(long codigo, long quantidade, BigDecimal valorUnitario, BigDecimal porcentagemCashback, long codigoDisco, String nomeDisco, long codigoCashback, DiaSemanaEnum dia) {
		super();
		this.codigo = codigo;
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
		this.porcentagemCashback = porcentagemCashback;
		this.codigoDisco = codigoDisco;
		this.nomeDisco = nomeDisco;
		this.codigoCashback = codigoCashback;
		this.dia = dia;
		this.valorTotal = valorUnitario.multiply(new BigDecimal(quantidade)).setScale(2, RoundingMode.HALF_DOWN);
		this.valorCashback = valorTotal.multiply(porcentagemCashback).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN);
	}
	
}
