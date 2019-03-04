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

import br.com.discos.dto.DiscoDetalheDTO;
import br.com.discos.dto.DiscoListagemDTO;
import br.com.discos.model.Disco;
import br.com.discos.model.Disco_;
import br.com.discos.model.Genero_;
import br.com.discos.repository.query.DiscoRepositoryQuery;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiscoRepositoryQueryImpl implements DiscoRepositoryQuery {

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public Page<DiscoListagemDTO> listar(@NonNull Pageable pagable, long codigoGenero) throws Exception{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DiscoListagemDTO> criteria = builder.createQuery(DiscoListagemDTO.class);
		Root<Disco> root = criteria.from(Disco.class);
		criteria.select(builder.construct(DiscoListagemDTO.class, 
				root.get(Disco_.codigo), 
				root.get(Disco_.nome), 
				root.get(Disco_.preco)));
		Predicate[] predicates = null;
		if (codigoGenero > 0){
			predicates = new Predicate[1];
			predicates[0] = builder.equal(root.get(Disco_.genero).get(Genero_.codigo), codigoGenero);
			criteria.where(predicates);
		}
		criteria.orderBy(builder.asc(root.get(Disco_.nome)));
		TypedQuery<DiscoListagemDTO> query = null;
		List<DiscoListagemDTO> dto = new ArrayList<DiscoListagemDTO>();
		try {
			query = entityManager.createQuery(criteria);
			montarPaginacao(query, pagable);
			dto = query.getResultList();
		}catch (Exception e) {
			log.error("Erro ao buscar lista de discos pelo genero {} na pagina {} de tamanho {}", codigoGenero, pagable.getPageNumber(), pagable.getPageSize());
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return new PageImpl<DiscoListagemDTO>(dto, pagable, verificarTotalRegistrosQuery(predicates));
	}

	@Override
	public DiscoDetalheDTO buscarPorCodigo(long codigo) throws Exception{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DiscoDetalheDTO> criteria = builder.createQuery(DiscoDetalheDTO.class);
		Root<Disco> root = criteria.from(Disco.class);
		criteria.select(builder.construct(DiscoDetalheDTO.class, 
				root.get(Disco_.codigo), 
				root.get(Disco_.nome), 
				root.get(Disco_.preco), 
				root.get(Disco_.genero).get(Genero_.nome)));
		criteria.where(builder.equal(root.get(Disco_.codigo), codigo));
		TypedQuery<DiscoDetalheDTO> query = null;
		DiscoDetalheDTO dto = new DiscoDetalheDTO();
		try {
			query = entityManager.createQuery(criteria);
			dto = query.getSingleResult();
		}catch (NoResultException e) {
			dto = null;
			log.info("Nao ha disco cadatrado com o codigo {}", codigo);
		}catch (Exception e) {
			log.error("Erro ao buscar o disco {}", codigo);
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return dto;
	}

	private long verificarTotalRegistrosQuery(Predicate[] predicates) throws Exception{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Disco> root = criteria.from(Disco.class);
		criteria.select(builder.count(root.get(Disco_.codigo)));
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
	
	private void montarPaginacao(TypedQuery<?> query, Pageable pagable) {
		int paginaAtual = pagable.getPageNumber();
		int registrosPorPagina = pagable.getPageSize();
		int primeiroRegistro = paginaAtual * registrosPorPagina;
		query.setFirstResult(primeiroRegistro);
		query.setMaxResults(registrosPorPagina);
	}
	
}
