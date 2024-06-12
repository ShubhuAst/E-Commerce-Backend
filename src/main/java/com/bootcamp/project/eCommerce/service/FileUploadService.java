package com.bootcamp.project.eCommerce.service;

import com.bootcamp.project.eCommerce.constants.FileFor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadService {

    final String UPLOAD_DIR = new ClassPathResource("/static/images").getFile().getAbsolutePath();

    public FileUploadService() throws IOException {
    }

    public void uploadFile(MultipartFile multipartFile, List<MultipartFile> secondaryFile, FileFor fileFor, Long id) throws IOException {

        if (multipartFile.isEmpty()) {
            return;
        }
        String fileType = multipartFile.getContentType();
        assert fileType != null;
        Files.copy(multipartFile.getInputStream(),
                Paths.get(
                        UPLOAD_DIR +
                                File.separator +
                                fileFor.getPath() +
                                File.separator +
                                id + "." +
                                fileType.substring(fileType.lastIndexOf("/") + 1)),
                StandardCopyOption.REPLACE_EXISTING);
    }

    public String getPrimaryFile(FileFor fileFor, Long id, String fileExtension) {

        String imagePath = UPLOAD_DIR + File.separator + fileFor.getPath() + File.separator + id + "." + fileExtension;
        File file = new File(imagePath);

        if (!file.exists()) {
            return ServletUriComponentsBuilder.fromCurrentContextPath().path(fileFor.getDefaultImagePath()).toUriString();
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(imagePath).toUriString();
    }
}
