package com.smartavaas.repository;

import com.smartavaas.model.CategoryFile;
import com.smartavaas.model.ManageFileDoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryFileRepository extends JpaRepository<CategoryFile, Integer> {
    Optional<CategoryFile> findByCategoryName(String categoryName);
}



