package com.epi.coreservice.dto;

import com.epi.coreservice.enums.OfferStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferDto {
    private String id;
    private String userId;
    private String product;
    private int offeredPrice;
    private OfferStatus status;
}
