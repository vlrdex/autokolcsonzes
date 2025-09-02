package com.example.autokolcsonzes.Service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final String path="/home/tothpeter/Pictures/auto";

    public Resource getImage(String filename) throws MalformedURLException {

        Path path = Paths.get(this.path).resolve(filename + ".jpg").normalize();
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            path = Paths.get(this.path).resolve(filename + ".jpeg").normalize();
            resource = new UrlResource(path.toUri());
        }

        return resource;

    }

    public boolean saveImage(int id, MultipartFile image){
        if (image.isEmpty()) {
            return false;
        }

       String extension = checkAndGetExtension(image);

        File destination = new File(path, id + "." + extension);

        try {
            image.transferTo(destination);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteImage(int id) {

        String[] extensions = {"jpg", "jpeg"};

        boolean deleted = false;

        for (String ext : extensions) {
            File file = new File(path, id + "." + ext);
            if (file.exists()) {
                deleted = file.delete() || deleted;
            }
        }

        return deleted;
    }

    public boolean modifyImage(int id, MultipartFile image) {

        if (checkExtension(image)){

            deleteImage(id);

            return saveImage(id, image);
        }else {
            return false;
        }

    }

    public String checkAndGetExtension(MultipartFile image){
        String originalFilename = image.getOriginalFilename();
        if (originalFilename.isBlank()) {
            return null;
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        if(extension.equals("jpg") || extension.equals("jpeg")){
            return extension;
        }else {
            return null;
        }
    }

    public boolean checkExtension(MultipartFile image) {
        String originalFilename = image.getOriginalFilename();
        if (originalFilename.isBlank()) {
            return false;
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        return extension.equals("jpg") || extension.equals("jpeg");

    }
}
