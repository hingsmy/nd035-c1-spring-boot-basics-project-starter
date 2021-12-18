package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @PostMapping("/save")
    public String handleNoteSubmit(Authentication authentication, Note note) {

        if (authentication != null) {

            User user = userService.getUser(authentication.getName());
            Integer userId = user.getUserId();
            note.setUserId(userId);
        }

        noteService.createNote(note);

        return "redirect:/";
    }
}
