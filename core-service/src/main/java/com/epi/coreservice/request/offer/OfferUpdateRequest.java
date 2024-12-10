package com.epi.coreservice.request.offer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferUpdateRequest {
    @NotBlank(message = "Offer id is required")
    private String id;
    private int offeredPrice;
}
