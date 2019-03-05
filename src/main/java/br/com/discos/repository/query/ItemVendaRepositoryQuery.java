package br.com.discos.repository.query;

import java.util.List;

import br.com.discos.dto.ItemVendaDetalheDTO;
import br.com.discos.dto.TotalItemVendaDTO;

public interface ItemVendaRepositoryQuery {

	public List<ItemVendaDetalheDTO> listarItensVendaPorCodigoVenda(long codigoVenda)throws Exception;
	public List<TotalItemVendaDTO> buscarTotaisItemVenda(long codigoVenda)throws Exception;
	
}
