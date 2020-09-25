package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

public class Blog {

    private final UUID id;

    @NotBlank
    private final String title;

    @NotBlank
    private final String description;

    private final String category;

    private LocalDate date;

    public Blog(@JsonProperty("id") UUID id,
                @JsonProperty("title") String title,
                @JsonProperty("description") String description,
                @JsonProperty("category") String category,
                @JsonProperty("date") LocalDate date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }
}
