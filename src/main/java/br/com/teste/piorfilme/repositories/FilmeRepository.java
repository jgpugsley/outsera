package br.com.teste.piorfilme.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.teste.piorfilme.entity.Filme;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Integer> {

}
