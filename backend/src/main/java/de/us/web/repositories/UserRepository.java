package de.us.web.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.us.web.domain.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByName(String name);

	Boolean existsByName(String name);

	Boolean existsByEmail(String email);
}