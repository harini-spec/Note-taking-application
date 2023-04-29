package io.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.notes.entity.UserDtls;

@Repository
public interface UserRepository extends JpaRepository<UserDtls, Integer>{
	
	public UserDtls findByEmail(String email);

}
