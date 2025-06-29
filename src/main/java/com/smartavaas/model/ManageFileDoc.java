package com.smartavaas.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ManageFileDoc {
    @Id
    private String fileId; // UUID

    private String fileName; // path or original file name
    private String docURL;   // downloadable URL




    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryFile category;

    private boolean active;


    private String createdBy;
    private String modifiedBy;

    public String getModifiedBy() {
        return modifiedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public CategoryFile getCategory() {
        return category;
    }

    public void setCategory(CategoryFile category) {
        this.category = category;
    }

    public String getDocURL() {
        return docURL;
    }

    public void setDocURL(String docURL) {
        this.docURL = docURL;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    private LocalDateTime modifiedAt;


    private LocalDateTime createdAt;
}

