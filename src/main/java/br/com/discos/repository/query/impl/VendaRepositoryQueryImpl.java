package br.com.discos.repository.query.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.discos.dto.VendaDetalheDTO;
import br.com.discos.dto.VendaListagemDTO;
import br.com.discos.model.Venda;
import br.com.discos.model.Venda_;
import br.com.discos.repository.query.VendaRepositoryQuery;
import br.com.discos.repository.query.filtro.FiltroListagemVenda;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VendaRepositoryQueryImpl implements VendaRepositoryQuery {

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public Page<VendaListagemDTO> listar(@NonNull Pageable pagable, @NonNull FiltroListagemVenda filtro) throws Exception{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<VendaListagemDTO> criteria = builder.createQuery(VendaListagemDTO.class);
		Root<Venda> root = criteria.from(Venda.class);
		criteria.select(builder.construct(VendaListagemDTO.class, 
				root.get(Venda_.codigo), 
				root.get(Venda_.data)));
		Predicate[] predicates = null;
		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null){
			predicates = new Predicate[1];
			predicates[0] = builder.between(root.get(Venda_.data), filtro.getDataInicial(), filtro.getDataFinal());
			criteria.where(predicates);
		}else {
			if (filtro.getDataInicial() != null) {
				predicates = new Predicate[1];
				predicates[0] = builder.greaterThanOrEqualTo(root.get(Venda_.data), filtro.getDataInicial());
				criteria.where(predicates);
			}else if (filtro.getDataFinal() != null) {
				predicates = new Predicate[1];
				predicates[0] = builder.lessThanOrEqualTo(root.get(Venda_.data), filtro.getDataFinal());
				criteria.where(predicates);
			}
		}
		criteria.orderBy(builder.desc(root.get(Venda_.data)));
		TypedQuery<VendaListagemDTO> query = null;
		List<VendaListagemDTO> dto = new ArrayList<VendaListagemDTO>();
		try {
			query = entityManager.createQuery(criteria);
			montarPaginacao(query, pagable);
			dto = query.getResultList();
		}catch (Exception e) {
			log.error("Erro ao buscar lista de vendas no periodo {} na página {} de tamanho {}", filtro.toString(), pagable.getPageNumber(), pagable.getPageSize());
			log.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return new PageImpl<VendaListagemDTO>(dto, pagable, verificarTotalRegistrosQuery(predicates));
	}

	@Override
	public VendaDetalheDTO buscarPorCodigo(long codigo) throws Exception{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<VendaDetalheDTO> criteria = builder.createQuery(VendaDetalheDTO.class);
		Root<Venda> root = criteria.from(Venda.class);
		criteria.select(builder.construct(VendaDetalheDTO.class, 
				root.get(Venda_.codigo), 
				root.get(Venda_.data)));
		criteria.where(builder.equal(root.get(Venda_.codigo), codigo));
		TypedQuery<VendaDetalheDTO> query = null;
		VendaDetalheDTO dto = new VendaDetalheDTO();
		try {
			query = entityManager.createQuery(criteria);
			dto = query.getSingleResult();
		}catch (NoResultException e) {
			dto = null;
		}catch (Exception e) {
			log.error("Erro ao buscar a venda {}", codigo);
			log.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return dto;
	}

	private long verificarTotalRegistrosQuery(Predicate[] predicates) throws Exception{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Venda> root = criteria.from(Venda.class);
		criteria.select(builder.count(root.get(Venda_.codigo)));
		if (predicates != null) {
			criteria.where(predicates);
		}
		long quantidade = 0;
		try {
			quantidade = entityManager.createQuery(criteria).getSingleResult();
		}catch (Exception e) {
			log.error("erro na contagem dos registros");
			log.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return quantidade;
	}
	
	private void montarPaginacao(TypedQuery<?> query, Pageable pagable){
		int paginaAtual = pagable.getPageNumber();
		int registrosPorPagina = pagable.getPageSize();
		int primeiroRegistro = paginaAtual * registrosPorPagina;
		query.setFirstResult(primeiroRegistro);
		query.setMaxResults(registrosPorPagina);
	}
	
}
