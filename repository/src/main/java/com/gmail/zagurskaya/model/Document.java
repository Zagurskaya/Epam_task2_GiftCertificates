package com.gmail.zagurskaya.model;

import java.util.UUID;

public class Document {

    private Long id;
    private String uniqueNumber;
    private String description;

    public Document() {
        this.uniqueNumber = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
