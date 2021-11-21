package br.com.fiap.Spare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.Spare.model.Tip;

public interface TipRepository extends JpaRepository<Tip, Long>{
	
}
