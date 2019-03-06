package br.com.discos.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class DiscoCadastradoDTO {

	private long codigo;
	private String nome;
	private BigDecimal preco;
	private long idGenero;
	private String nomeGenero;
	
}
