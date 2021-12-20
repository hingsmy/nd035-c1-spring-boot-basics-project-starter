package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    NoteService noteService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String getHomePage(Authentication authentication, Model model) {

        User user = new User();
        if (authentication != null) {
            String username = authentication.getName();
            user = userService.getUser(username);
            List<Note> notes = noteService.getNotes(user);

            model.addAttribute("notes", notes);
        }

        model.addAttribute("user", user);


        Note aNote = new Note();

        model.addAttribute("aNote", aNote);

        return "home";
    }



}
