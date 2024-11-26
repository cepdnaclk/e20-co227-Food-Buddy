package com.foodBudy_v2.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadImage(String path, MultipartFile file) throws IOException;

    byte[] downloadPhotoFromFileSystem(String path, String fileName) throws IOException;

    void deleteImage(String path, String fileName);
}
