package com.epi.coreservice.request.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentCreateRequest {
    @NotBlank(message = "Comment name is required")
    private String name;
    private String description;
    @NotBlank(message = "User id is required")
    private String userId;
    @NotBlank(message = "Product id is required")
    private String productId;
}
