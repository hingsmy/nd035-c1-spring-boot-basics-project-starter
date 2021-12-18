package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    NoteMapper noteMapper;

    public boolean isNoteExit(Integer noteId) {
        return noteMapper.getNote(noteId) != null;
    }


    public int saveNote(Note note) {

        if (isNoteExit(note.getNoteId())) {

            return noteMapper.update(note);
        } else {
            return noteMapper.insert(note);
        }

    }

    public List<Note> getNotes(User user) {

        return noteMapper.getAllNotes(user);
    }

    public void deleteNote(Integer theId) {

        noteMapper.delete(theId);
    }
}
