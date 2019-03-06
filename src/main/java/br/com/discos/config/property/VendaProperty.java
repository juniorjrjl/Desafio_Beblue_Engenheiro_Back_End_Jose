package br.com.discos.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("venda")
public class VendaProperty {

	@Getter
	private final Spotify spotify = new Spotify();
	
	@Getter @Setter
	public static class Spotify{
		private String clienteId;
		private String clienteSecret;
		private String limit;
	}
	
}
