package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        noteService.saveNote(note);

        return "result";
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam("id") Integer theId) {

        noteService.deleteNote(theId);
        return "result";
    }

}
