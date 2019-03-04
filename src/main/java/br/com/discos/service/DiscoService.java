package br.com.discos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.discos.dto.DiscoDetalheDTO;
import br.com.discos.dto.DiscoListagemDTO;
import br.com.discos.repository.DiscoRepository;
import lombok.NonNull;

@Service
public class DiscoService {

	@Autowired
	private DiscoRepository discoRepository;
	
	public Page<DiscoListagemDTO> listar(@NonNull Pageable pagable, long codigoGenero) throws Exception{
		Page<DiscoListagemDTO> dto = null;
		try {
			dto = discoRepository.listar(pagable, codigoGenero);
		}catch (Exception e) {
			throw e;
		}
		return dto;
	}
	
	public DiscoDetalheDTO buscarPorCodigo(long codigo) throws Exception{
		DiscoDetalheDTO dto = null;
		try {
			dto = discoRepository.buscarPorCodigo(codigo);
		}catch (Exception e) {
			throw e;
		}
		return dto;
	}
	
}
