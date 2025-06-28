package com.smartavaas.controller;

import com.nimbusds.jose.util.Resource;
import com.smartavaas.common.ApiResponseBuilder;
import com.smartavaas.dto.BaseApiResponse;
import com.smartavaas.model.ManageFileDoc;
import com.smartavaas.repository.ManageFileDocRepository;
import com.smartavaas.service.DocumentCentreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/upload")
    public ResponseEntity<BaseApiResponse<String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category,
            Principal principal // comes from AuthBeaver or Spring Security
    ) throws IOException {
        String username = principal.getName();
        try {
            String result = documentService.uploadDocument(username, file, category);
            return ResponseEntity.ok(ApiResponseBuilder.success("File is uploaded successfully",result));

        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseBuilder.error(e.getMessage(), HttpStatus.BAD_REQUEST, "exeception occured in file upload"));

        }
    }

    @GetMapping("/download/{fileId}")
    public byte[] downloadFile(@PathVariable String fileId) throws IOException {
        ManageFileDoc doc = (ManageFileDoc) manageFileDocRepository.findByFileId(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        return Files.readAllBytes(Paths.get(doc.getDocURL()));
    }

}


