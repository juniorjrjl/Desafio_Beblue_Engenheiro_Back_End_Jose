package br.com.discos.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.discos.dto.VendaDetalheDTO;
import br.com.discos.dto.VendaListagemDTO;
import br.com.discos.repository.query.filtro.FiltroListagemVenda;

public interface VendaRepositoryQuery {

	public Page<VendaListagemDTO> listar(Pageable pagable, FiltroListagemVenda filtro) throws Exception;
	public VendaDetalheDTO buscarPorCodigo(long codigo)throws Exception;
	
}
