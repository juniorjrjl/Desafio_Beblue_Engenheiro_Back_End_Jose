package br.com.discos.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class DiscoCadastroDTO {

	private String nome;
	private BigDecimal preco;
	private long idGenero;
	
}
