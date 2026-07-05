package com.patrick.desafio_java_jr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivroRequestDTO {

	@NotBlank(message = "O titulo e obrigatorio.")
	private String titulo;

	@NotBlank(message = "O autor e obrigatorio.")
	private String autor;

	@NotNull(message = "O ano de publicacao e obrigatorio.")
	private Integer anoPublicacao;
}