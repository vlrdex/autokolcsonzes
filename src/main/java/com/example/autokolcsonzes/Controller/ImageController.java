package com.example.autokolcsonzes.Controller;

import com.example.autokolcsonzes.Service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService){
        this.imageService=imageService;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {

        Resource resource=imageService.getImage(filename);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<String> modifyImage(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file) {

        boolean success = imageService.modifyImage(id, file);

        if (!success) {
            return ResponseEntity
                    .badRequest()
                    .body("Sikertelen mentés: csak JPG vagy JPEG fájl engedélyezett!");
        }

        return ResponseEntity
                .ok("A kép sikeresen elmentve");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable int id) {

        boolean deleted = imageService.deleteImage(id);

        if (deleted) {
            return ResponseEntity.ok("A kép sikeresen törölve");
        } else {
            return ResponseEntity
                    .notFound()
                    .build();
        }

    }


}
