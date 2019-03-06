package br.com.discos.repository.query.filtro;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FiltroListagemVenda {

	private LocalDate dataInicial = null;
	private LocalDate dataFinal = null;
	
}
