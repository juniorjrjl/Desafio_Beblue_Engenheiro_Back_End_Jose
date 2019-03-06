package db.migration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;

import br.com.discos.model.DiaSemanaEnum;


public class V201903051052__CriacaoTabelasProjetoInsercaoCashbacks extends BaseJavaMigration{

	private final String TABELA_CASHBACK = "create table cashbacks (\r\n" + 
			"    codigo bigint not null auto_increment,\r\n" + 
			"    dia integer not null,\r\n" + 
			"    porcentagem decimal(5,2) not null check (porcentagem<=100 AND porcentagem>=0),\r\n" + 
			"    codigo_genero bigint not null,\r\n" + 
			"    primary key (codigo)\r\n" + 
			");";
	
	private final String TABELA_DISCOS = "create table discos (\r\n" + 
			"    codigo bigint not null auto_increment,\r\n" + 
			"    nome varchar(200) not null,\r\n" + 
			"    preco decimal(10,2) not null check (preco>=0),\r\n" + 
			"    codigo_genero bigint not null,\r\n" + 
			"    primary key (codigo)\r\n" + 
			");";
	
	private final String TABELA_GENEROS = "create table generos (\r\n" + 
			"    codigo bigint not null auto_increment,\r\n" + 
			"    nome varchar(200) not null,\r\n" + 
			"    primary key (codigo)\r\n" + 
			");";
	
	private final String TABELA_ITENS_VENDA = "create table itens_vendas (\r\n" + 
			"    codigo bigint not null auto_increment,\r\n" + 
			"    porcentagem_cashback decimal(10,2) not null check (porcentagem_cashback<=100 AND porcentagem_cashback>=0),\r\n" + 
			"    quantidade bigint not null check (quantidade>=1),\r\n" + 
			"    valor_unitario decimal(10,2) not null check (valor_unitario>=0),\r\n" + 
			"    codigo_cashback bigint not null,\r\n" + 
			"    codigo_disco bigint not null,\r\n" + 
			"    codigo_venda bigint not null,\r\n" + 
			"    primary key (codigo)\r\n" + 
			");";
	
	private final String TABELA_VENDAS = "create table vendas (\r\n" + 
			"    codigo bigint not null auto_increment,\r\n" + 
			"    data date not null,\r\n" + 
			"    primary key (codigo)\r\n" + 
			");";
	
	private final String FK_CASHBACKS_GENEROS = "	alter table cashbacks add constraint fk_cashbacks_generos foreign key (codigo_genero) references generos (codigo);";
	
	private final String FK_DISCOS_GENEROS ="alter table discos add constraint fk_discos_generos foreign key (codigo_genero) references generos (codigo);";
	   
	private final String FK_ITENS_VENDAS_CASHBACKS = "alter table itens_vendas add constraint fk_itens_vendas_cashbacks foreign key (codigo_cashback) references cashbacks (codigo);";
	   
	private final String FK_ITENS_VENDAS_DISCOS = "alter table itens_vendas add constraint fk_itens_vendas_discos foreign key (codigo_disco) references discos (codigo);";
	   
	private final String FK_ITENS_VENDAS_VENDAS = "alter table itens_vendas add constraint fk_itens_vendas_vendas foreign key (codigo_venda) references vendas (codigo);";
	
	private final String INSERT_GENEROS = "insert into generos (nome) values (?);";
	
	private final String  INSERT_CASHBACKS = "insert into cashbacks (porcentagem, dia, codigo_genero) values (?, ?, ?);";
	
	private final String SELECT_GENEROS = "select codigo\r\n"
		  + "  from generos"
		  + " where nome = ?";
	
	private final String POP = "pop";
	private final String MPB = "mpb";
	private final String CLASSICAL = "classical";
	private final String ROCK = "rock";
	
	@Override
	public void migrate(Context context) throws Exception {
		JdbcTemplate template = new JdbcTemplate(context.getConnection());
		template.execute(TABELA_CASHBACK);
		template.execute(TABELA_DISCOS);
		template.execute(TABELA_GENEROS);
		template.execute(TABELA_ITENS_VENDA);
		template.execute(TABELA_VENDAS);
		template.execute(FK_CASHBACKS_GENEROS);
		template.execute(FK_DISCOS_GENEROS);
		template.execute(FK_ITENS_VENDAS_CASHBACKS);
		template.execute(FK_ITENS_VENDAS_DISCOS);
		template.execute(FK_ITENS_VENDAS_VENDAS);
		List<String> generos = Arrays.asList(POP, MPB, CLASSICAL, ROCK);
		generos.forEach(g -> {
			try {
				template.execute(INSERT_GENEROS, g);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		inserirCashbackClassic(template);
		inserirCashbackMPB(template);
		inserirCashbackPOP(template);
		inserirCashbackRock(template);
	}

	private void inserirCashbackPOP(JdbcTemplate template) throws Exception {
		long idGenero = template.queryForInt(SELECT_GENEROS, POP);
		template.execute(INSERT_CASHBACKS, new BigDecimal(25).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.DOMINGO.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(7).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SEGUNDA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(6).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.TERCA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(2).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.QUARTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(10).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.QUINTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(15).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SEXTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(20).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SABADO.getCodigo(), idGenero);
	}
	
	private void inserirCashbackMPB(JdbcTemplate template) throws Exception {
		long idGenero = template.queryForInt(SELECT_GENEROS, MPB);
		template.execute(INSERT_CASHBACKS, new BigDecimal(30).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.DOMINGO.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(5).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SEGUNDA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(10).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.TERCA.getCodigo(), idGenero);	
		template.execute(INSERT_CASHBACKS, new BigDecimal(15).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.QUARTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(20).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.QUINTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(25).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SEXTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(30).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SABADO.getCodigo(), idGenero);
	}
	
	private void inserirCashbackClassic(JdbcTemplate template) throws Exception {
		long idGenero = template.queryForInt(SELECT_GENEROS, CLASSICAL);
		template.execute(INSERT_CASHBACKS, new BigDecimal(35).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.DOMINGO.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(3).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SEGUNDA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(5).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.TERCA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(8).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.QUARTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(13).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.QUINTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(18).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SEXTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(25).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SABADO.getCodigo(), idGenero);
	}
	
	private void inserirCashbackRock(JdbcTemplate template) throws Exception {
		long idGenero = template.queryForInt(SELECT_GENEROS, ROCK);
		template.execute(INSERT_CASHBACKS, new BigDecimal(40).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.DOMINGO.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(10).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SEGUNDA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(15).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.TERCA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(15).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.QUARTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(15).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.QUINTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(20).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SEXTA.getCodigo(), idGenero);
		template.execute(INSERT_CASHBACKS, new BigDecimal(40).setScale(2, RoundingMode.HALF_DOWN), DiaSemanaEnum.SABADO.getCodigo(), idGenero);
	}
	
}
