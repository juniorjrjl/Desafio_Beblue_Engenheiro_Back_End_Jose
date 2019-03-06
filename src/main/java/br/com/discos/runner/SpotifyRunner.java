package br.com.discos.runner;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.discos.config.property.VendaProperty;
import br.com.discos.dto.DiscoCadastroDTO;
import br.com.discos.dto.GeneroListagemDTO;
import br.com.discos.dto.spotify.Token;
import br.com.discos.exception.DiscoServiceExcepion;
import br.com.discos.service.DiscoService;
import br.com.discos.service.GeneroService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("!test")
public class SpotifyRunner implements ApplicationRunner {
	
	@Autowired
	private VendaProperty VendaProperty;
	
	@Autowired
	private GeneroService generoService;
	
	@Autowired
	private DiscoService discoService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (discoService.QuantidadeDiscosCadastrados() == 0) {
			log.info("buscando discos para popular a base");
			Token token = obterAccessTokenSpotify();
			LocalDateTime dataObtencaoToken = LocalDateTime.now();
			log.info("token obtido em {}", dataObtencaoToken.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
			List<GeneroListagemDTO> dto = generoService.listar();
			dto.forEach(g ->{
				log.info("buscando genero {}", g.getNome());
				List<String> discosNomes = buscarDiscosSpotify(g.getNome(), token);
				discosNomes.forEach(d -> {
					try {
						discoService.cadastrar(new DiscoCadastroDTO(d, new BigDecimal(Math.random() * (59.99)), g.getCodigo()));
					} catch (DiscoServiceExcepion e) {
						log.error(e.getMessage());
					}
				});
			});
			log.info("fim do cadastro dos discos");
		}else {
			log.info("A base de dados já foi populada uma primeira vez");
		}
	}

	private List<String> buscarDiscosSpotify(String categoria, Token token) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + token.getAccessToken());
		HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
		String url = String.format("https://api.spotify.com/v1/browse/categories/%s/playlists?offset=0&limit=%s", categoria, VendaProperty.getSpotify().getLimit());
		List<String> nomesAlbuns = new ArrayList<String>();
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			nomesAlbuns= lerNomeAlbuns(response.getBody());
		}catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return nomesAlbuns;
	}
	
	private List<String> lerNomeAlbuns(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		List<String> nomesAlbuns = new ArrayList<String>(); 
		try {
			JsonNode json = mapper.readTree(jsonString);
			for (int i = 0; i < json.get("playlists").get("items").size(); i++) {
				nomesAlbuns.add(json.get("playlists").get("items").get(i).get("name").textValue());
			}
		} catch (IOException e) {
			log.error("erro ao montar o json");
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return nomesAlbuns;
	}
	
	private String getBase64(String texto) {
		byte[] encoded = Base64.encodeBase64(texto.getBytes(Charset.forName("US-ASCII")));
		return new String(encoded);
	}
	private Token obterAccessTokenSpotify() {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic " + getBase64(String.format("%s:%s", VendaProperty.getSpotify().getClienteId(), VendaProperty.getSpotify().getClienteSecret())));
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>(); 
		body.add("grant_type", "client_credentials");
		Token token = null;
		try {
			log.info("buscando token de autenticação no spotify");
			ResponseEntity<Token> response = restTemplate.exchange("https://accounts.spotify.com/api/token", HttpMethod.POST, new HttpEntity<Object>(body, headers), Token.class);
			token = response.getBody();
		}catch (Exception e) {
			log.error("Erro ao recuperar o token");
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return token;
		
	}
	
}
