package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Controller
@RequestMapping("/files")
public class FileController {

    private final Path fileStorageDir;

    public FileController(@Value("./files/") Path fileStorageDir) {
        this.fileStorageDir = fileStorageDir;
    }

    @PostConstruct
    public void ensureDirectoryExists() throws IOException {
        if (!Files.exists(this.fileStorageDir)) {
            Files.createDirectories(this.fileStorageDir);
        }
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file")MultipartFile file) {

        // check if file is empty
        if (file.isEmpty()) {
            return "redirect:/";
        }

        // normalise the file path
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));


        //save the file on the local file system
        try {
            String UPLOAD_DIR = "./files/";
            Path path = Paths.get(UPLOAD_DIR + fileName);
            System.out.println(path);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "result";
    }
}
