package br.com.discos.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class VendaCadastroDTO {

	public LocalDateTime dataHora;
	public List<ItemVendaCadastroDTO> itemVenda = new ArrayList<ItemVendaCadastroDTO>(); 
	
}
