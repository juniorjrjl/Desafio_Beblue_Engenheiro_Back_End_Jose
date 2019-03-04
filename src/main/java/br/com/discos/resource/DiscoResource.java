package br.com.discos.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.discos.dto.DiscoDetalheDTO;
import br.com.discos.dto.DiscoListagemDTO;
import br.com.discos.service.DiscoService;

@RestController
@RequestMapping("/discos")
public class DiscoResource {

	@Autowired
	private DiscoService discoService;
	
	@GetMapping("/")
	public ResponseEntity<Page<DiscoListagemDTO>> listar(@Param("codigoGenero") Long codigoGenero, Pageable pagable){
		try {
			Page<DiscoListagemDTO> dto = discoService.listar(pagable, (codigoGenero != null)? codigoGenero.longValue() : 0l);
			return (dto.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<DiscoDetalheDTO> buscarPorCodigo(@PathVariable long codigo){
		try {
			DiscoDetalheDTO dto = discoService.buscarPorCodigo(codigo);
			return (dto == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
