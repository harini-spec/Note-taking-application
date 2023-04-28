package io.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.notes.entity.UserDetails;

@Repository
public interface UserRepository extends JpaRepository<UserDetails, Integer>{

}
