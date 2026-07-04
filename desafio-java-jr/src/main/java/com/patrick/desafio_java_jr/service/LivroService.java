package com.patrick.desafio_java_jr.service;

import com.patrick.desafio_java_jr.dto.LivroRequestDTO;
import com.patrick.desafio_java_jr.dto.LivroResponseDTO;
import com.patrick.desafio_java_jr.entity.Livro;
import com.patrick.desafio_java_jr.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LivroService {

	private final LivroRepository livroRepository;

	public LivroResponseDTO criar(LivroRequestDTO requestDTO) {
		Livro livro = new Livro();
		livro.setTitulo(requestDTO.getTitulo());
		livro.setAutor(requestDTO.getAutor());
		livro.setAnoPublicacao(requestDTO.getAnoPublicacao());

		Livro livroSalvo = livroRepository.save(livro);
		return toResponseDTO(livroSalvo);
	}

	public List<LivroResponseDTO> listarTodos() {
		return livroRepository.findAll()
				.stream()
				.map(this::toResponseDTO)
				.toList();
	}

	public LivroResponseDTO atualizarPorId(Long id, LivroRequestDTO requestDTO) {
		Livro livro = livroRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Livro nao encontrado com o id: " + id));

		livro.setTitulo(requestDTO.getTitulo());
		livro.setAutor(requestDTO.getAutor());
		livro.setAnoPublicacao(requestDTO.getAnoPublicacao());

		Livro livroAtualizado = livroRepository.save(livro);
		return toResponseDTO(livroAtualizado);
	}

	public void deletarPorId(Long id) {
		Livro livro = livroRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Livro nao encontrado com o id: " + id));

		livroRepository.delete(livro);
	}

	private LivroResponseDTO toResponseDTO(Livro livro) {
		return new LivroResponseDTO(
				livro.getId(),
				livro.getTitulo(),
				livro.getAutor(),
				livro.getAnoPublicacao()
		);
	}
}