package com.example.sevice.batis.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClothsSize {
    private String size;
    private String inseam;

    public ClothsSize() {
    }

    public ClothsSize(String size, String inseam) {
        this.size = size;
        this.inseam = inseam;
    }

    // getters and setters
}