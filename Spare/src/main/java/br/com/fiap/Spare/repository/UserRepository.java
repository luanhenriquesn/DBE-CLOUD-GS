package br.com.fiap.Spare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.Spare.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String username);

}
