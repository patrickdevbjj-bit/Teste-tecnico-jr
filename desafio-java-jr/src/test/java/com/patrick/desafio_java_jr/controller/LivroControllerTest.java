package com.patrick.desafio_java_jr.controller;

import com.patrick.desafio_java_jr.dto.LivroRequestDTO;
import com.patrick.desafio_java_jr.dto.LivroResponseDTO;
import com.patrick.desafio_java_jr.exception.GlobalExceptionHandler;
import com.patrick.desafio_java_jr.exception.LivroNotFoundException;
import com.patrick.desafio_java_jr.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LivroControllerTest {

	private MockMvc mockMvc;

	private LivroService livroService;

	@BeforeEach
	void setUp() {
		livroService = mock(LivroService.class);

		mockMvc = MockMvcBuilders
				.standaloneSetup(new LivroController(livroService))
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void shouldCreateLivro() throws Exception {
		LivroResponseDTO responseDTO = new LivroResponseDTO(
				1L,
				"Revolucao dos Bichos",
				"George Orwell",
				1945
		);

		when(livroService.criar(any(LivroRequestDTO.class))).thenReturn(responseDTO);

		mockMvc.perform(post("/livros")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "titulo": "Revolucao dos Bichos",
								  "autor": "George Orwell",
								  "anoPublicacao": 1945
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/livros/1"))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.titulo").value("Revolucao dos Bichos"))
				.andExpect(jsonPath("$.autor").value("George Orwell"))
				.andExpect(jsonPath("$.anoPublicacao").value(1945));
	}

	@Test
	void shouldListAllLivros() throws Exception {
		List<LivroResponseDTO> livros = List.of(
				new LivroResponseDTO(1L, "Revolucao dos Bichos", "George Orwell", 1945)
		);

		when(livroService.listarTodos()).thenReturn(livros);

		mockMvc.perform(get("/livros"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].titulo").value("Revolucao dos Bichos"));
	}

	@Test
	void shouldUpdateLivroById() throws Exception {
		LivroResponseDTO responseDTO = new LivroResponseDTO(
				1L,
				"Revolucao dos Bichos - Edicao Atualizada",
				"George Orwell",
				1946
		);

		when(livroService.atualizarPorId(eq(1L), any(LivroRequestDTO.class))).thenReturn(responseDTO);

		mockMvc.perform(put("/livros/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "titulo": "Revolucao dos Bichos - Edicao Atualizada",
								  "autor": "George Orwell",
								  "anoPublicacao": 1946
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.titulo").value("Revolucao dos Bichos - Edicao Atualizada"))
				.andExpect(jsonPath("$.autor").value("George Orwell"))
				.andExpect(jsonPath("$.anoPublicacao").value(1946));
	}

	@Test
	void shouldDeleteLivroById() throws Exception {
		doNothing().when(livroService).deletarPorId(1L);

		mockMvc.perform(delete("/livros/{id}", 1L))
				.andExpect(status().isNoContent());
	}

	@Test
	void shouldReturnNotFoundWhenLivroDoesNotExist() throws Exception {
		doThrow(new LivroNotFoundException(999L)).when(livroService).deletarPorId(999L);

		mockMvc.perform(delete("/livros/{id}", 999L))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.erro").value("Livro nao encontrado com o id: 999"));
	}

	@Test
	void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {
		mockMvc.perform(post("/livros")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "titulo": "",
								  "autor": "",
								  "anoPublicacao": null
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.titulo").value("O titulo e obrigatorio."))
				.andExpect(jsonPath("$.autor").value("O autor e obrigatorio."))
				.andExpect(jsonPath("$.anoPublicacao").value("O ano de publicacao e obrigatorio."));
	}
}
