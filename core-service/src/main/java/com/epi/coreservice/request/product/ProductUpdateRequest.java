package com.epi.coreservice.request.product;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductUpdateRequest {
    @NotBlank(message = "Product id is required")
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private String[] keys;
}
