package com.patrick.desafio_java_jr.repository;

import com.patrick.desafio_java_jr.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {
}
