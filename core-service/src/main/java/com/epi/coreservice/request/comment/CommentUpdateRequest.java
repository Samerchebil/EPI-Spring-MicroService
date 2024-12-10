package com.epi.coreservice.request.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentUpdateRequest {
    @NotBlank(message = "Comment id is required")
    private String id;
    private String name;
    private String description;
}
