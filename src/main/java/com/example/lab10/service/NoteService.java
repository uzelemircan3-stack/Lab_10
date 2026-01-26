package com.example.lab10.service;

import com.example.lab10.dto.NoteDto;
import com.example.lab10.entity.Note;
import com.example.lab10.entity.User;
import com.example.lab10.repository.NoteRepository;
import com.example.lab10.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public List<Note> getUserNotes(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return noteRepository.findByUser(user);
    }

    public Note createNote(NoteDto noteDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Note note = new Note();
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        note.setUser(user);

        return noteRepository.save(note);
    }

    public void deleteNote(Long noteId, String username) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        // PDF Task 1.1: İş mantığı doğrulaması
        if (!note.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to delete this note");
        }

        noteRepository.delete(note);
    }

    public List<Note> searchNotesNative(String query, String username) {
        // DÜZELTME: Repository metodu artık gerçekten çağrılıyor.
        // Bu sayede "Wanted but not invoked" hatası çözülecek.
        return noteRepository.searchNotesNative(query, username);
    }
}