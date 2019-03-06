package br.com.discos.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.discos.dto.DiscoCadastroDTO;
import br.com.discos.dto.VendaCadastroDTO;
import br.com.discos.model.Disco;
import br.com.discos.model.Venda;

@Configuration
public class MapeamentoObjeto {

	@Bean(name = "org.modelmapper.ModelMapper")
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.createTypeMap(DiscoCadastroDTO.class, Disco.class).addMappings(m -> {
			m.map(src -> src.getNome(), Disco::setNome);
			m.map(src -> src.getPreco(), Disco::setPreco);
			m.<Long>map(src -> src.getIdGenero(), (d, v) -> d.getGenero().setCodigo(v));
		});
		modelMapper.createTypeMap(VendaCadastroDTO.class, Venda.class).addMappings(m -> {
			m.map(src -> src.getData(), Venda::setData);
		});
		return modelMapper;
	}

}
