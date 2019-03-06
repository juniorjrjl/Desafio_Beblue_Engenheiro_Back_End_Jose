package br.com.discos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.discos.dto.GeneroListagemDTO;
import br.com.discos.repository.GeneroRepository;

@Service
public class GeneroService {

	@Autowired
	private GeneroRepository generoRepository;
	
	public List<GeneroListagemDTO> listar(){
		return generoRepository.buscarNomesCodigos();
	}
	
}
