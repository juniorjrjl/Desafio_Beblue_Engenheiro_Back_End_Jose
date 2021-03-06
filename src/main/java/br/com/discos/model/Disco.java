package br.com.discos.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"codigo", "nome"})
@ToString(exclude = {"itensVenda"})
@Table
@Entity(name = "discos")
public class Disco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private long codigo;
	
	@Column(nullable = false, length = 200)
	private String nome;
	
	@Column(nullable = false, precision = 10, scale  = 2)
	private BigDecimal preco;
	
	@ManyToOne
	@JoinColumn(name = "codigo_genero", nullable = false)
	private Genero genero = new Genero();
	
	@OneToMany(mappedBy = "disco", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<ItemVenda> itensVenda = new ArrayList<ItemVenda>();
	
}
