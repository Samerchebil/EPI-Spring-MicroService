package com.epi.coreservice.request.product;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductCreateRequest {
    @NotBlank(message = "Product name is required")
    private String name;
    private String description;
    @NotBlank(message = "Category id is required")
    private String categoryId;
    private String[] keys;
}
