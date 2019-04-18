package com.xuexiang.springbootgrpcapi.service.impl;

import com.xuexiang.springbootgrpcapi.config.FileStorageProperties;
import com.xuexiang.springbootgrpcapi.exception.FileNotFoundException;
import com.xuexiang.springbootgrpcapi.exception.FileStorageException;
import com.xuexiang.springbootgrpcapi.service.FileStorageService;
import com.xuexiang.springbootgrpcapi.utils.FileIOUtils;
import com.xuexiang.springbootgrpcapi.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service(value = "fileService")
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    private final FileStorageProperties fileStorageProperties;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getFileDirectory()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String storeFile(String fileName, long fileSize, byte[] data) throws Exception {
        if (!fileStorageProperties.isKeepName()){ //不保持文件名
            fileName = FileUtils.randomFileName(fileName);
        }
        // Check if the file's name contains invalid characters
        if(fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        // Copy file to the target location (Replacing existing file with the same name)
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        FileUtils.createOrExistsFile(targetLocation.toFile());
        FileIOUtils.writeFileFromBytesByChannel(targetLocation.toFile().getPath(), data);
        return fileName;
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }


}
