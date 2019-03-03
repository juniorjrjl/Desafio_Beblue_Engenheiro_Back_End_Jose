package br.com.discos.repository.query.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.discos.dto.ItemVendaDetalheDTO;
import br.com.discos.dto.ItemVendaRegistradaDTO;
import br.com.discos.dto.TotalItemVendaDTO;
import br.com.discos.model.Cashback_;
import br.com.discos.model.Disco_;
import br.com.discos.model.ItemVenda;
import br.com.discos.model.ItemVenda_;
import br.com.discos.model.Venda_;
import br.com.discos.repository.query.ItemVendaRepositoryQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemVendaRepositoryQueryImpl implements ItemVendaRepositoryQuery {

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public List<ItemVendaDetalheDTO> listarItensVendaPorCodigoVenda(long codigoVenda) throws Exception{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ItemVendaDetalheDTO> criteria = builder.createQuery(ItemVendaDetalheDTO.class);
		Root<ItemVenda> root = criteria.from(ItemVenda.class);
		criteria.select(builder.construct(ItemVendaDetalheDTO.class, 
				root.get(ItemVenda_.codigo), 
				root.get(ItemVenda_.quantidade), 
				root.get(ItemVenda_.valorUnitario),
				root.get(ItemVenda_.porcentagemCashback),
				root.get(ItemVenda_.disco).get(Disco_.codigo),
				root.get(ItemVenda_.disco).get(Disco_.nome),
				root.get(ItemVenda_.cashback).get(Cashback_.codigo),
				root.get(ItemVenda_.cashback).get(Cashback_.dia)));
		criteria.where(builder.equal(root.get(ItemVenda_.venda).get(Venda_.codigo), codigoVenda));
		criteria.orderBy(builder.asc(root.get(ItemVenda_.disco).get(Disco_.nome)));
		TypedQuery<ItemVendaDetalheDTO> query = null;
		List<ItemVendaDetalheDTO> dto = new ArrayList<ItemVendaDetalheDTO>();
		try {
			query = entityManager.createQuery(criteria);
			dto = query.getResultList();
		}catch (Exception e) {
			log.error("Erro ao buscar os itens da venda {}", codigoVenda);
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return dto;
	}

	@Override
	public List<ItemVendaRegistradaDTO> listarItensVendaRecemRegistrada(long codigoVenda) throws Exception{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ItemVendaRegistradaDTO> criteria = builder.createQuery(ItemVendaRegistradaDTO.class);
		Root<ItemVenda> root = criteria.from(ItemVenda.class);
		criteria.select(builder.construct(ItemVendaRegistradaDTO.class, 
				root.get(ItemVenda_.codigo), 
				root.get(ItemVenda_.quantidade), 
				root.get(ItemVenda_.valorUnitario),
				root.get(ItemVenda_.porcentagemCashback),
				root.get(ItemVenda_.disco).get(Disco_.codigo),
				root.get(ItemVenda_.disco).get(Disco_.nome),
				root.get(ItemVenda_.cashback).get(Cashback_.codigo),
				root.get(ItemVenda_.cashback).get(Cashback_.dia)));
		criteria.where(builder.equal(root.get(ItemVenda_.venda).get(Venda_.codigo), codigoVenda));
		criteria.orderBy(builder.asc(root.get(ItemVenda_.disco).get(Disco_.nome)));
		TypedQuery<ItemVendaRegistradaDTO> query = null;
		List<ItemVendaRegistradaDTO> dto = new ArrayList<ItemVendaRegistradaDTO>();
		try {
			query = entityManager.createQuery(criteria);
			dto = query.getResultList();
		}catch (Exception e) {
			log.error("Erro ao buscar os itens da venda {}", codigoVenda);
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return dto;
	}

	public List<TotalItemVendaDTO> buscarTotaisItemVenda(long codigoVenda) throws Exception{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TotalItemVendaDTO> criteria = builder.createQuery(TotalItemVendaDTO.class);
		Root<ItemVenda> root = criteria.from(ItemVenda.class);
		criteria.select(builder.construct(TotalItemVendaDTO.class, 
				root.get(ItemVenda_.valorUnitario), 
				root.get(ItemVenda_.porcentagemCashback),
				root.get(ItemVenda_.quantidade)));
		criteria.where(builder.equal(root.get(ItemVenda_.venda).get(Venda_.codigo), codigoVenda));
		TypedQuery<TotalItemVendaDTO> query = null;
		List<TotalItemVendaDTO> dto = new ArrayList<TotalItemVendaDTO>();
		try {
			query = entityManager.createQuery(criteria);
			dto = query.getResultList();
		}catch (Exception e) {
			log.error("Erro ao buscar o disco {}", codigoVenda);
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return dto;
	}
	
}
