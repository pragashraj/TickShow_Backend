package com.tickshow.backend.utils;

import com.tickshow.backend.exception.FileNotFoundException;
import com.tickshow.backend.exception.FileStorageException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@NoArgsConstructor
public class FileStorageService {
    private Path fileStorageLocation;
    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    public FileStorageService(String pathString) throws FileStorageException {
        this.fileStorageLocation = Paths.get("uploads/" + pathString).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored", ex);
        }
    }

    public String storeFile(MultipartFile file) throws FileStorageException {
        String fileName = StringUtils.cleanPath(System.currentTimeMillis() + file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException | FileStorageException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public ResponseEntity<Resource> loadFileAsResource(String fileName, HttpServletRequest request) throws FileNotFoundException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {

                String contentType = null;

                try {
                    contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                } catch (IOException ex) {
                    log.error("Could not determine file type");
                }

                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException | FileNotFoundException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }

    public byte[] convert(String fileName) throws IOException {
        File convFile = new File(fileStorageLocation.toString(), fileName);
        byte[] bytesArray = new byte[(int) convFile.length()];
        FileInputStream fis = new FileInputStream(convFile);
        fis.read(bytesArray);
        fis.close();
        return bytesArray;
    }
}