package com.epi.coreservice.request.offer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MakeAnOfferRequest {
    @NotBlank(message = "User id is required")
    private String userId;
    @NotBlank(message = "Comment id is required")
    private String productId;
    @NotNull(message = "Offered price is required")
    private int offeredPrice;
}
