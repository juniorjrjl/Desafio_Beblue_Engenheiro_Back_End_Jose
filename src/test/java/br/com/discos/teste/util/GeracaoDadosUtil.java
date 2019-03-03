package br.com.discos.teste.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


import br.com.discos.model.Cashback;
import br.com.discos.model.DiaSemanaEnum;
import br.com.discos.model.Disco;
import br.com.discos.model.Genero;
import br.com.discos.model.ItemVenda;
import br.com.discos.model.Venda;

public class GeracaoDadosUtil {
	
	public static List<Genero> criarGeneros(int quantidade){
		List<Genero> generos = new ArrayList<Genero>();
		for (int i = 0; i < quantidade; i ++) {
			Genero genero = new Genero();
			genero.setNome("genero " + i);
			generos.add(genero);
		}
		return generos;
	}
	
	public static List<Cashback> criarCashbacks(List<Genero> generos) {
		List<Cashback> cashbacks = new ArrayList<Cashback>();
		for (Genero g : generos) {
			List<DiaSemanaEnum> diasSemana = Arrays.asList(DiaSemanaEnum.values());
			for(DiaSemanaEnum d : diasSemana) {
				Cashback cashback = new Cashback();
				cashback.setDia(d);
				cashback.getGenero().setCodigo(g.getCodigo());
				cashback.setPorcentagem(new BigDecimal((Math.random() * (99.9))).setScale(2, RoundingMode.HALF_EVEN));
				cashbacks.add(cashback);
			}
		}
		return cashbacks;
	}

	public static List<Disco> criarDiscos(int quantidade, List<Genero> generos) {
		List<Disco> discos = new ArrayList<Disco>();
		for (int i = 0; i < quantidade; i++) {
			Disco disco = new Disco();
			disco.setNome("disco " + i);
			double min = 0.00;
			double max = 200;
			double preco = (Math.random() * (max - min) + 1) + min;
			disco.setPreco(new BigDecimal(preco).setScale(2, RoundingMode.HALF_DOWN));
			disco.getGenero().setCodigo(generos.get(new Random().nextInt(generos.size() - 1) + 1).getCodigo());
			discos.add(disco);
		}
		return discos;
	}

	public static List<Venda> criarVendas(int quantidade, List<Disco> discos, List<Cashback> cashbacks) {
		List<Venda> vendas = new ArrayList<Venda>();
		for (int i = 0; i < quantidade; i++) {
			Venda venda = new Venda();
			LocalDateTime dataVenda = LocalDateTime.of(2018, 1, new Random().nextInt(30) + 1, 0, 0, 0);
			venda.setDataHoraVenda(dataVenda);
			List<ItemVenda> itensVenda = new ArrayList<ItemVenda>();
			for (int z = 0; z < 2; z ++) {
				ItemVenda itemVenda = new ItemVenda();
				Cashback cashback = cashbacks.get(new Random().nextInt(cashbacks.size() - 1) + 1);
				Disco disco = discos.get(new Random().nextInt(discos.size() - 1) + 1);
				itemVenda.getCashback().setCodigo(cashback.getCodigo());
				itemVenda.getDisco().setCodigo(disco.getCodigo());
				itemVenda.setPorcentagemCashback(cashback.getPorcentagem());
				itemVenda.setQuantidade(new Random().nextInt(10) + 1);
				itemVenda.setValorUnitario(disco.getPreco());
				itemVenda.setVenda(venda);
				itensVenda.add(itemVenda);
			}
			venda.setItensVenda(itensVenda);
			vendas.add(venda);
		}
		return vendas;
	}

}
