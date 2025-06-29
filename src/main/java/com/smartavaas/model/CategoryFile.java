package com.smartavaas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORY_FILE")  // optional if your table name matches
public class CategoryFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(nullable = false, unique = true)
    private String categoryName; // e.g., RENT, REALESTATE

    // --- Getter and Setter ---
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
