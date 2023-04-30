package io.notes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.notes.entity.Notes;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer> {
	
	@Query("from Notes as n where n.userDtls.id=:uid") // user id is taken as uid -- to get notes of a particular user 
	Page<Notes> findNotesByUser(@Param("uid") int uid, Pageable p);

}
