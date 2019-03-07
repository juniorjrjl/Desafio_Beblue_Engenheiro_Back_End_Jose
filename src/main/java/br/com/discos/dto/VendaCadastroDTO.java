package br.com.discos.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = {"itensVenda"})
@NoArgsConstructor
@Getter @Setter
public class VendaCadastroDTO {

	@NotNull
	public LocalDate data;
	
	@NotNull
	@Size(min = 1)
	@Valid
	public List<ItemVendaCadastroDTO> itensVenda = new ArrayList<ItemVendaCadastroDTO>(); 
	
}
