package br.com.discos.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"codigo", "dia"})
@ToString
@Table
@Entity(name = "cashbacks")
public class Cashback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private long codigo;
	
	@NotNull
	@Min(value = 0)
	@Max(value = 100)
	@Column(nullable = false, precision = 5, scale  = 2)
	private BigDecimal porcentagem;
	
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private DiaSemanaEnum dia;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_genero", nullable = false)
	private Genero genero = new Genero();
	
}
