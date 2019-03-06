package br.com.discos.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.discos.dto.VendaCadastroDTO;
import br.com.discos.dto.VendaDetalheDTO;
import br.com.discos.dto.VendaListagemDTO;
import br.com.discos.repository.query.filtro.FiltroListagemVenda;
import br.com.discos.service.VendaService;

@RestController
@RequestMapping("/vendas")
public class VendaResource {

	@Autowired
	private VendaService vendaService;
	
	@GetMapping
	public ResponseEntity<Page<VendaListagemDTO>> listar(Pageable pagable, FiltroListagemVenda filtro){
		try {
			Page<VendaListagemDTO> dto = vendaService.listar(pagable, filtro);
			return (dto.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<VendaDetalheDTO> buscarPorCodigo(@PathVariable long codigo){
		try {
			VendaDetalheDTO dto = vendaService.buscarPorCodigo(codigo);
			return (dto == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping
	public ResponseEntity<VendaDetalheDTO> cadastrar(@Valid @RequestBody VendaCadastroDTO dtoVendaCadastrar){
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.cadastrar(dtoVendaCadastrar));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
