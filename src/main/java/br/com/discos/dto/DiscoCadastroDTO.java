package br.com.discos.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class DiscoCadastroDTO {

	@NotBlank
	@Size(min = 1, max = 200)
	@NotNull
	private String nome;
	
	@NotNull
	@Min(value = 0)
	private BigDecimal preco;
	
	private long idGenero;
	
}
