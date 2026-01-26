package com.example.lab10.service;

import com.example.lab10.entity.Note;
import com.example.lab10.entity.User;
import com.example.lab10.repository.NoteRepository;
import com.example.lab10.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void getUserNotes_Success() {
        String username = "marvin";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(noteRepository.findByUser(any())).thenReturn(Arrays.asList(new Note()));

        List<Note> result = noteService.getUserNotes(username);

        assertFalse(result.isEmpty());
        verify(userRepository).findByUsername(username);
    }
}