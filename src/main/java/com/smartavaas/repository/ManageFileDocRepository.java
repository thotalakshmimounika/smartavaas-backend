package com.smartavaas.repository;

import com.smartavaas.model.CategoryFile;
import com.smartavaas.model.ManageFileDoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ManageFileDocRepository extends JpaRepository<ManageFileDoc, String> {
    Optional<Object> findByFileId(String fileId);
    List<ManageFileDoc> findAllByCreatedBy(String createdBy);


}


