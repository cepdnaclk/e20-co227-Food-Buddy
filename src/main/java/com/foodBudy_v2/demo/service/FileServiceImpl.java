package com.foodBudy_v2.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // get the file names of current / original file
        String originalFileName = file.getOriginalFilename();;

        // generate a unique file name
        String randomId = UUID.randomUUID().toString();
        // mat.jpg --> .jpg
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        // 1234 --> 1234.jpg
        String fileName = randomId.concat(fileExtension);
        String filePath = path + File.separator + fileName;
        // why not /? -> might not work in other OSs.

        // check if path exists
        File folder = new File(path);

        if (!folder.exists()){
            folder.mkdir();
        }

        // upload to server
        //input stream --> destination
        Files.copy(file.getInputStream(), Paths.get(filePath));

        //return the file name
        return fileName;
    }

    @Override
    public byte[] downloadPhotoFromFileSystem(String path, String fileName) throws IOException {

        String filePath = path + File.separator + fileName;

        byte[] photo = Files.readAllBytes(new File(filePath).toPath());

        return photo;
    }

}
