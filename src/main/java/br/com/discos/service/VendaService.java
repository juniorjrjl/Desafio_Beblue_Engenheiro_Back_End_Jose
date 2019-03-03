package br.com.discos.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.discos.dto.TotalItemVendaDTO;
import br.com.discos.dto.VendaDetalheDTO;
import br.com.discos.dto.VendaListagemDTO;
import br.com.discos.repository.ItemVendaRepository;
import br.com.discos.repository.VendaRepository;
import br.com.discos.repository.query.filtro.FiltroListagemVenda;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VendaService {

	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private ItemVendaRepository itemVendaRepository;
	
	
	public Page<VendaListagemDTO> listar(@NonNull Pageable pagable, @NonNull FiltroListagemVenda filtro) throws Exception{
		Page<VendaListagemDTO> dto = null;
		try {
			dto = vendaRepository.listar(pagable, filtro); 
			if (dto != null && !dto.isEmpty()) {
				dto.getContent().forEach(d -> {
					List<TotalItemVendaDTO> totais = null;
					try {
						totais = itemVendaRepository.buscarTotaisItemVenda(d.getCodigo());
						d.setValorTotal(totais.stream().map(m -> m.getValorItem()).reduce(BigDecimal.ZERO, BigDecimal::add));
						d.setValorCashback(totais.stream().map(m -> m.getValoCashback()).reduce(BigDecimal.ZERO, BigDecimal::add));
					} catch (Exception e) {
						log.error("erro na geração dos totais da venda {}", d.getCodigo());
						log.error(ExceptionUtils.getStackTrace(e));
					}
				});
			}
		}catch (Exception e) {
			log.error("Erro na busca de vendas pelo periodo {} na página {} de tamanho {}", filtro.toString(), pagable.getPageNumber(), pagable.getPageSize());
			log.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return dto;
	}
	
	public VendaDetalheDTO buscarPorCodigo(long codigo) throws Exception{
		VendaDetalheDTO dto = null;
		try {
			dto = vendaRepository.buscarPorCodigo(codigo);
			if (dto != null) {
				dto.setItensVenda(itemVendaRepository.listarItensVendaPorCodigoVenda(codigo));
				List<TotalItemVendaDTO> totais = itemVendaRepository.buscarTotaisItemVenda(codigo);
				if (totais != null && !totais.isEmpty()) {
					dto.setValorTotal(totais.stream().map(m -> m.getValorItem()).reduce(BigDecimal.ZERO, BigDecimal::add));
					dto.setValorCashback(totais.stream().map(m -> m.getValoCashback()).reduce(BigDecimal.ZERO, BigDecimal::add));
				}
			}
		}catch (Exception e) {
			log.error("Erro ao buscar a venda {}", codigo);
			log.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return dto;
	}
	
}
