package com.example.autokolcsonzes.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest {

    @TempDir
    Path tempDir;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageService();
        imageService.path = tempDir.toString();
    }

    @Test
    void testSaveAndGetImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "fake-image-content".getBytes()
        );

        boolean saved = imageService.saveImage(1, file);
        assertTrue(saved);

        Resource resource = imageService.getImage("1");
        assertTrue(resource.exists());
    }

    @Test
    void testDeleteImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpeg", "image/jpeg", "fake".getBytes()
        );

        imageService.saveImage(2, file);

        boolean deleted = imageService.deleteImage(2);
        assertTrue(deleted);

        Resource resource=imageService.getImage("2");
        assertFalse(resource.exists());
    }

    @Test
    void testModifyImage() {
        MockMultipartFile oldFile = new MockMultipartFile(
                "file", "old.jpg", "image/jpeg", "old".getBytes()
        );
        imageService.saveImage(3, oldFile);

        MockMultipartFile newFile = new MockMultipartFile(
                "file", "new.jpg", "image/jpeg", "new".getBytes()
        );
        boolean modified = imageService.modifyImage(3, newFile);

        assertTrue(modified);
        File stored = new File(tempDir.toFile(), "3.jpg");
        assertThat(stored).exists();
    }

    @Test
    void testCheckAndGetExtension() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "name.jpeg", "image/jpeg", "content".getBytes()
        );
        String ext = imageService.checkAndGetExtension(file);
        assertEquals("jpeg", ext);
    }

    @Test
    void testCheckExtension_invalid() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "bad.png", "image/png", "content".getBytes()
        );
        assertFalse(imageService.checkExtension(file));
    }
}
