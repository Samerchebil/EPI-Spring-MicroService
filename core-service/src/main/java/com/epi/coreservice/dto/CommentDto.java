package com.epi.coreservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    private String id;
    private String name;
    private String description;
    private String userId;
    private List<String> imagesId;
}
