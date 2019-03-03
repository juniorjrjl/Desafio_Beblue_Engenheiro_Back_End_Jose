package br.com.discos.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"codigo", "nome"})
@ToString(exclude = {"discos", "cashbacks"})
@Table
@Entity(name = "generos")
public class Genero {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private long codigo;
	
	@Size(min = 1, max = 200)
	@NotNull
	@Column(nullable = false, length = 200)
	private String nome;
	
	@OneToMany(mappedBy = "genero", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Disco> discos = new ArrayList<Disco>();
	
	@OneToMany(mappedBy = "genero", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Cashback> cashbacks = new ArrayList<Cashback>();
	
}
