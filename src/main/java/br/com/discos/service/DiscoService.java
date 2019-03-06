package br.com.discos.service;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.discos.dto.DiscoCadastroDTO;
import br.com.discos.dto.DiscoDetalheDTO;
import br.com.discos.dto.DiscoListagemDTO;
import br.com.discos.exception.DiscoServiceExcepion;
import br.com.discos.model.Disco;
import br.com.discos.repository.DiscoRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiscoService {

	@Autowired
	private DiscoRepository discoRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Validator validator;
	
	public long QuantidadeDiscosCadastrados() {
		return discoRepository.count();
	}
	
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
	
	public void cadastrar(@NonNull DiscoCadastroDTO dto) throws DiscoServiceExcepion {
		Set<ConstraintViolation<DiscoCadastroDTO>> violacoes = validator.validate(dto);
		if (!violacoes.isEmpty()) {
			throw new DiscoServiceExcepion(String.format("O disco (%s) teve as seguintes restrições violadas : %s", String.join(", ", violacoes.stream().map(m -> m.getMessageTemplate()).collect(Collectors.toList())), dto.toString()));
		}
		try {
			discoRepository.save(mapper.map(dto, Disco.class));
		}catch (Exception e) {
			log.error("Erro ao cadastrar o disco", dto.toString());
			log.error(ExceptionUtils.getStackTrace(e));
		}
	}
	
}
