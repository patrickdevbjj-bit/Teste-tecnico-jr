package com.patrick.desafio_java_jr.service;

import com.patrick.desafio_java_jr.dto.LivroRequestDTO;
import com.patrick.desafio_java_jr.dto.LivroResponseDTO;
import com.patrick.desafio_java_jr.entity.Livro;
import com.patrick.desafio_java_jr.exception.LivroNotFoundException;
import com.patrick.desafio_java_jr.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LivroServiceTest {

	private LivroRepository livroRepository;
	private LivroService livroService;

	@BeforeEach
	void setUp() {
		livroRepository = mock(LivroRepository.class);
		livroService = new LivroService(livroRepository);
	}

	@Test
	void shouldCreateLivro() {
		LivroRequestDTO requestDTO = new LivroRequestDTO("Revolucao dos Bichos", "George Orwell", 1945);
		Livro livroSalvo = new Livro(1L, "Revolucao dos Bichos", "George Orwell", 1945);

		when(livroRepository.save(any(Livro.class))).thenReturn(livroSalvo);

		LivroResponseDTO responseDTO = livroService.criar(requestDTO);

		assertEquals(1L, responseDTO.getId());
		assertEquals("Revolucao dos Bichos", responseDTO.getTitulo());
		assertEquals("George Orwell", responseDTO.getAutor());
		assertEquals(1945, responseDTO.getAnoPublicacao());
		verify(livroRepository, times(1)).save(any(Livro.class));
	}

	@Test
	void shouldListAllLivros() {
		List<Livro> livros = List.of(
				new Livro(1L, "Revolucao dos Bichos", "George Orwell", 1945)
		);

		when(livroRepository.findAll()).thenReturn(livros);

		List<LivroResponseDTO> response = livroService.listarTodos();

		assertEquals(1, response.size());
		assertEquals("Revolucao dos Bichos", response.get(0).getTitulo());
		verify(livroRepository, times(1)).findAll();
	}

	@Test
	void shouldUpdateLivroById() {
		Long id = 1L;
		Livro livroExistente = new Livro(id, "Revolucao dos Bichos", "George Orwell", 1945);
		LivroRequestDTO requestDTO = new LivroRequestDTO("Revolucao dos Bichos - Edicao Atualizada", "George Orwell", 1946);
		Livro livroAtualizado = new Livro(id, "Revolucao dos Bichos - Edicao Atualizada", "George Orwell", 1946);

		when(livroRepository.findById(id)).thenReturn(Optional.of(livroExistente));
		when(livroRepository.save(any(Livro.class))).thenReturn(livroAtualizado);

		LivroResponseDTO response = livroService.atualizarPorId(id, requestDTO);

		assertEquals(id, response.getId());
		assertEquals("Revolucao dos Bichos - Edicao Atualizada", response.getTitulo());
		assertEquals("George Orwell", response.getAutor());
		assertEquals(1946, response.getAnoPublicacao());
		verify(livroRepository, times(1)).findById(id);
		verify(livroRepository, times(1)).save(any(Livro.class));
	}

	@Test
	void shouldThrowExceptionWhenUpdatingNonexistentLivro() {
		Long id = 999L;
		LivroRequestDTO requestDTO = new LivroRequestDTO("Revolucao dos Bichos", "George Orwell", 1945);

		when(livroRepository.findById(id)).thenReturn(Optional.empty());

		LivroNotFoundException exception = assertThrows(
				LivroNotFoundException.class,
				() -> livroService.atualizarPorId(id, requestDTO)
		);

		assertEquals("Livro nao encontrado com o id: 999", exception.getMessage());
		verify(livroRepository, times(1)).findById(id);
	}

	@Test
	void shouldDeleteLivroById() {
		Long id = 1L;
		Livro livroExistente = new Livro(id, "Revolucao dos Bichos", "George Orwell", 1945);

		when(livroRepository.findById(id)).thenReturn(Optional.of(livroExistente));
		doNothing().when(livroRepository).delete(livroExistente);

		livroService.deletarPorId(id);

		verify(livroRepository, times(1)).findById(id);
		verify(livroRepository, times(1)).delete(livroExistente);
	}

	@Test
	void shouldThrowExceptionWhenDeletingNonexistentLivro() {
		Long id = 999L;

		when(livroRepository.findById(id)).thenReturn(Optional.empty());

		LivroNotFoundException exception = assertThrows(
				LivroNotFoundException.class,
				() -> livroService.deletarPorId(id)
		);

		assertEquals("Livro nao encontrado com o id: 999", exception.getMessage());

		verify(livroRepository, times(1)).findById(id);
	}
}
