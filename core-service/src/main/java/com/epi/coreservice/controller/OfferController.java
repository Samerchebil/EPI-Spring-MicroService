package com.epi.coreservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.epi.coreservice.dto.OfferDto;
import com.epi.coreservice.request.offer.MakeAnOfferRequest;
import com.epi.coreservice.request.offer.OfferUpdateRequest;
import com.epi.coreservice.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/core-service/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;
    private final ModelMapper modelMapper;

    @PostMapping("/makeAnOffer")
    public ResponseEntity<OfferDto> makeAnOffer(@Valid @RequestBody MakeAnOfferRequest request) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(offerService.makeAnOffer(request), OfferDto.class));
    }

    @GetMapping("/getOfferById/{id}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(offerService.getOfferById(id), OfferDto.class));
    }

    @GetMapping("/getOffersByUserId/{id}")
    public ResponseEntity<List<OfferDto>> getOffersByUserId(@PathVariable String id) {
        return ResponseEntity.ok(offerService.getOffersByUserId(id).stream()
                .map(offer -> modelMapper.map(offer, OfferDto.class)).toList());
    }

    @GetMapping("/getOffersByProductId/{id}")
    public ResponseEntity<List<OfferDto>> getOffersByProductId(@PathVariable String id) {
        return ResponseEntity.ok(offerService.getOffersByProductId(id).stream()
                .map(offer -> modelMapper.map(offer, OfferDto.class)).toList());
    }

    @PutMapping("/update")
    public ResponseEntity<OfferDto> updateOfferById(@Valid @RequestBody OfferUpdateRequest request) {
        return ResponseEntity.ok(modelMapper.map(offerService.updateOfferById(request), OfferDto.class));
    }

    @DeleteMapping("/deleteOfferById/{id}")
    public ResponseEntity<Void> deleteOfferById(@PathVariable String id) {
        offerService.deleteOfferById(id);
        return ResponseEntity.ok().build();
    }
}
