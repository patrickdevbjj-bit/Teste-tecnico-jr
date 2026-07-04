package com.patrick.desafio_java_jr.controller;

import com.patrick.desafio_java_jr.dto.LivroRequestDTO;
import com.patrick.desafio_java_jr.dto.LivroResponseDTO;
import com.patrick.desafio_java_jr.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

	private final LivroService livroService;

	@PostMapping
	public ResponseEntity<LivroResponseDTO> criar(
			@Valid @RequestBody LivroRequestDTO requestDTO,
			UriComponentsBuilder uriBuilder
	) {
		LivroResponseDTO responseDTO = livroService.criar(requestDTO);
		URI uri = uriBuilder.path("/livros/{id}").buildAndExpand(responseDTO.getId()).toUri();

		return ResponseEntity.created(uri).body(responseDTO);
	}

	@GetMapping
	public ResponseEntity<List<LivroResponseDTO>> listarTodos() {
		return ResponseEntity.ok(livroService.listarTodos());
	}

	@PutMapping("/{id}")
	public ResponseEntity<LivroResponseDTO> atualizarPorId(
			@PathVariable Long id,
			@Valid @RequestBody LivroRequestDTO requestDTO
	) {
		return ResponseEntity.ok(livroService.atualizarPorId(id, requestDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
		livroService.deletarPorId(id);
		return ResponseEntity.noContent().build();
	}
}
