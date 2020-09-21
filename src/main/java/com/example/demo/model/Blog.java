package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Blog {

    private final UUID id;

    @NotBlank
    private final String title;

    @NotBlank
    private final String description;

    public Blog(@JsonProperty("id") UUID id,
                @JsonProperty("title") String title,
                @JsonProperty("description") String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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
}
