package br.com.discos.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.discos.dto.DiscoDetalheDTO;
import br.com.discos.dto.DiscoListagemDTO;

public interface DiscoRepositoryQuery {

	public Page<DiscoListagemDTO> listar(Pageable pagable, long idGenero) throws Exception;
	public DiscoDetalheDTO buscarPorCodigo(long codigo) throws Exception;
	
}
