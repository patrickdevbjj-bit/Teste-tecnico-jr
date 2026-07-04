package com.patrick.desafio_java_jr.exception;

public class LivroNotFoundException extends RuntimeException {

	public LivroNotFoundException(Long id) {
		super("Livro nao encontrado com o id: " + id);
	}
}