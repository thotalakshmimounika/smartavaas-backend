package com.smartavaas.service;

import com.smartavaas.model.CategoryFile;
import com.smartavaas.model.ManageFileDoc;
import com.smartavaas.repository.CategoryFileRepository;
import com.smartavaas.repository.ManageFileDocRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DocumentCentreService {

    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    private CategoryFileRepository categoryFileRepository;

    @Autowired
    private ManageFileDocRepository manageFileDocRepository;

    public String uploadDocument(String username, MultipartFile file, String categoryName) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");

        }

        if (file.getSize() > 2 * 1024 * 1024) { // 5MB max
            throw new IllegalArgumentException("File size exceeds limit");
        }

        // 2. Get or Create Category
        CategoryFile category = categoryFileRepository.findByCategoryName(categoryName)
                .orElseGet(() -> {
                    CategoryFile newCategory = new CategoryFile();
                    newCategory.setCategoryName(categoryName);
                    return categoryFileRepository.save(newCategory);
                });


        String filetime = java.time.format.DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")
                .format(LocalDateTime.now());

        String fileId = UUID.randomUUID().toString();

        String originalFileName = file.getOriginalFilename();
        String storedFileName = filetime + "_" +  fileId +"_"+originalFileName;

        Path path = Paths.get(uploadDir).resolve(storedFileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        ManageFileDoc doc = new ManageFileDoc();
        doc.setFileId(fileId);
        doc.setFileName(storedFileName);
        doc.setDocURL(path.toString());
        doc.setCategory(category);
        doc.setActive(true);
        doc.setCreatedBy(username);
        LocalDateTime time = LocalDateTime.now();
        doc.setCreatedAt(time);
        doc.setModifiedAt(time);
        doc.setModifiedBy(username);



        manageFileDocRepository.save(doc);

        return "File uploaded successfully with ID: " + fileId;
    }
}

