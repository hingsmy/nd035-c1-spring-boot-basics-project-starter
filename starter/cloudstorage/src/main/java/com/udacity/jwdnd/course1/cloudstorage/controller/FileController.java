package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

@Controller
@RequestMapping("/files")
public class FileController {

    private final Path fileStorageDir;
    private Path userFileStorageDir;

    public FileController(@Value("./files/") Path fileStorageDir) {
        this.fileStorageDir = fileStorageDir;
    }

    @Autowired
    UserService userService;

    @PostConstruct
    public void ensureDirectoryExists() throws IOException {
        if (!Files.exists(this.fileStorageDir)) {
            Files.createDirectories(this.fileStorageDir);
        }
    }

    @PostMapping(value = "/upload", produces = MediaType.TEXT_PLAIN_VALUE)
    public String uploadFile(Authentication authentication, @RequestParam("file") MultipartFile file) throws IOException {

        Integer userId = null;
        if (authentication != null) {

            User user = userService.getUser(authentication.getName());
            userId = user.getUserId();
        }

        this.userFileStorageDir = this.fileStorageDir.resolve(String.valueOf(userId));

        if (!Files.exists(userFileStorageDir)) {
            Files.createDirectories(userFileStorageDir);
        }

        String targetFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        final Path targetPath = userFileStorageDir.resolve(targetFileName);

        try (InputStream in = file.getInputStream()) {
            try (OutputStream out = Files.newOutputStream(targetPath, StandardOpenOption.CREATE)) {
                in.transferTo(out);
            }
        }

        System.out.println(targetFileName);

        return "result";
    }

    @GetMapping("{userid}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("userid") String userId, @PathVariable("fileName") String fileName) {


        final Path targetPath = this.userFileStorageDir.resolve(fileName);
        if (!Files.exists(targetPath)) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource resource = new FileSystemResource(targetPath);
        MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        ContentDisposition disposition = ContentDisposition.attachment().filename(Objects.requireNonNull(resource.getFilename())).build();
        headers.setContentDisposition(disposition);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

//    private static Optional<String> getFileExtension(String fileName) {
//        final int indexOfLastDot = fileName.lastIndexOf('.');
//
//        if (indexOfLastDot == -1) {
//            return Optional.empty();
//        } else {
//            return Optional.of(fileName.substring(indexOfLastDot + 1));
//        }
//    }
}
