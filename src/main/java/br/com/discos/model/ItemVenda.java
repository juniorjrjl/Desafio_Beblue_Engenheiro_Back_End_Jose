package br.com.discos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"codigo", "quantidade"})
@ToString
@Table
@Entity(name = "itens_vendas")
public class ItemVenda implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private long codigo;
	
	@Column(nullable = false)
	private long quantidade;
	
	@Column(nullable = false, precision = 10, scale  = 2)
	private BigDecimal valorUnitario;
	
	@Column(nullable = false, precision = 10, scale  = 2)
	private BigDecimal porcentagemCashback;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "codigo_venda", nullable = false)
	private Venda venda = new Venda();
	
	@ManyToOne
	@JoinColumn(name = "codigo_disco", nullable = false)
	private Disco disco = new Disco();
	
	@ManyToOne
	@JoinColumn(name = "codigo_cashback", nullable = false)
	private Cashback cashback = new Cashback();
	
}
