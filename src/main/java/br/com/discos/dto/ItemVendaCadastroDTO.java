package br.com.discos.dto;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ItemVendaCadastroDTO {

	@Min(value = 1)
	public long quantidade;
	
	@Min(value = 1)
	public long codigoDisco;
	
}
