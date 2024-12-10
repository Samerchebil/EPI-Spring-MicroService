package com.epi.coreservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private List<CommentDto> adverts;
    private List<String> keys;
    private List<String> imagesId;
}
