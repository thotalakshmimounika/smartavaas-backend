package com.smartavaas.controller;

import com.nimbusds.jose.util.Resource;
import com.smartavaas.common.ApiResponseBuilder;
import com.smartavaas.dto.BaseApiResponse;
import com.smartavaas.model.ManageFileDoc;
import com.smartavaas.repository.ManageFileDocRepository;
import com.smartavaas.service.DocumentCentreService;
//import org.hibernate.mapping.List;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/documentCentre")
public class DocumentCentreController {

    @Autowired
    private DocumentCentreService documentService;

    @Autowired
    private ManageFileDocRepository manageFileDocRepository;

    // Upload multiple files (max 10)
    @PostMapping("/uploadMultiple")
    public ResponseEntity<BaseApiResponse<String>> uploadMultipleFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("category") String category,
            Principal principal
    ) {
        String username = principal.getName();

        if (files.length > 10) {
            return ResponseEntity.badRequest().body(
                    ApiResponseBuilder.error("Maximum 10 files allowed", HttpStatus.BAD_REQUEST, null));
        }

        try {
            ArrayList<String> list_of_names = new ArrayList<>();
            for (MultipartFile file : files) {
                documentService.uploadDocument(username, file, category);
                list_of_names.add(file.getOriginalFilename());
            }

            return ResponseEntity.ok(ApiResponseBuilder.success("Files uploaded successfully", "uploaded files names are : " + list_of_names));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponseBuilder.error("Upload failed", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    //  Download file by fileId
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
        try {
            ManageFileDoc doc = (ManageFileDoc)manageFileDocRepository.findByFileId(fileId)
                    .orElseThrow(() -> new RuntimeException("File not found"));

            byte[] fileContent = Files.readAllBytes(Paths.get(doc.getDocURL()));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"");

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("X-Error", e.getMessage())
                    .build();
        }
    }

    //  View file inline by fileId (PDF, image, etc.)
    @GetMapping("/view/{fileId}")
    public ResponseEntity<byte[]> viewFile(@PathVariable String fileId) {
        try {
            ManageFileDoc doc = (ManageFileDoc)manageFileDocRepository.findByFileId(fileId)
                    .orElseThrow(() -> new RuntimeException("File not found"));

            byte[] fileContent = Files.readAllBytes(Paths.get(doc.getDocURL()));
            String contentType = Files.probeContentType(Paths.get(doc.getDocURL()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE));
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getFileName() + "\"");

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("X-Error", e.getMessage())
                    .build();
        }
    }

    // Get all files uploaded by the logged-in user (uses entity directly)
    @GetMapping("/myFiles")
    public ResponseEntity<BaseApiResponse<?>> listUserFiles(Principal principal) {
        String username = principal.getName();
        try {
            List<ManageFileDoc> files = manageFileDocRepository.findAllByCreatedBy(username);
            return ResponseEntity.ok(ApiResponseBuilder.success("User files fetched successfully", files));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseBuilder.error("Failed to fetch user files", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }
}