package com.example.lab10.repository;

import com.example.lab10.entity.Note;
import com.example.lab10.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUser(User user);

    // Testin hata vermemesi için bu metodun burada tanımlı olması şart:
    @Query(value = "SELECT * FROM notes WHERE (title LIKE %?1% OR content LIKE %?1%) AND user_id = (SELECT id FROM users WHERE username = ?2)", nativeQuery = true)
    List<Note> searchNotesNative(String query, String username);
}