package br.com.discos.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

import br.com.discos.dto.CashbackVendaDTO;
import br.com.discos.dto.DiscoPrecoGeneroDTO;
import br.com.discos.dto.ItemVendaCadastroDTO;
import br.com.discos.dto.TotalItemVendaDTO;
import br.com.discos.dto.VendaCadastroDTO;
import br.com.discos.dto.VendaDetalheDTO;
import br.com.discos.dto.VendaListagemDTO;
import br.com.discos.exception.VendaServiceException;
import br.com.discos.model.DiaSemanaEnum;
import br.com.discos.model.ItemVenda;
import br.com.discos.model.Venda;
import br.com.discos.repository.CashbackRepository;
import br.com.discos.repository.DiscoRepository;
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
	
	@Autowired
	private CashbackRepository cashbackRepository;
	
	@Autowired
	private DiscoRepository discoRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Validator validator;
	
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
	
	public VendaDetalheDTO cadastrar(VendaCadastroDTO dto) throws Exception {
		Set<ConstraintViolation<VendaCadastroDTO>> violacoes = validator.validate(dto);
		if (!violacoes.isEmpty()) {
			throw new VendaServiceException(String.format("A venda (%s) teve as seguintes restrições violadas : %s", String.join(", ", violacoes.stream().map(m -> m.getMessageTemplate()).collect(Collectors.toList())), dto.toString()));
		}
		Venda venda = new Venda();
		List<ItemVenda> itensVenda = new ArrayList<ItemVenda>();
		venda = mapper.map(dto, Venda.class);
		for(ItemVendaCadastroDTO i : dto.getItensVenda()) {
			ItemVenda itemVenda = new ItemVenda();
			DiscoPrecoGeneroDTO discoPrecoGenero = discoRepository.buscarValorDisco(i.getCodigoDisco());
			CashbackVendaDTO cashBackDia = cashbackRepository.buscarInformacoesCashbackDia(discoPrecoGenero.getCodigoGenero(), DiaSemanaEnum.values()[LocalDateTime.now().getDayOfWeek().getValue()]);
			itemVenda.setQuantidade(i.getQuantidade());
			itemVenda.setValorUnitario(discoPrecoGenero.getPreco());
			itemVenda.setPorcentagemCashback(cashBackDia.getPorcentagem());
			itemVenda.getDisco().setCodigo(discoPrecoGenero.getCodigoGenero());
			itemVenda.getCashback().setCodigo(cashBackDia.getCodigo());
			itemVenda.setVenda(venda);
			itensVenda.add(itemVenda);
		}
		venda.setItensVenda(itensVenda);
		vendaRepository.save(venda);
		return buscarPorCodigo(venda.getCodigo());
	}
	
}
