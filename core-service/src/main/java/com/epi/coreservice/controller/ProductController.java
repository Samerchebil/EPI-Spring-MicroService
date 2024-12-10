package com.epi.coreservice.controller;

import com.epi.coreservice.dto.ProductDto;
import com.epi.coreservice.request.product.ProductCreateRequest;
import com.epi.coreservice.request.product.ProductUpdateRequest;
import com.epi.coreservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/core-service/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ProductDto> createProduct(@RequestPart ProductCreateRequest request,
                                             @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(productService.createProduct(request, file), ProductDto.class));
    }

    @GetMapping("/getAll")
    ResponseEntity<List<ProductDto>> getAll() {
        return ResponseEntity.ok(productService.getAll().stream()
                .map(product -> modelMapper.map(product, ProductDto.class)).toList());
    }

    @GetMapping("/getProductById/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(productService.getProductById(id), ProductDto.class));
    }

    @GetMapping("/getProductsByCategoryId/{id}")
    ResponseEntity<List<ProductDto>> getProductsByCategoryId(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductsByCategoryId(id).stream()
                .map(product -> modelMapper.map(product, ProductDto.class)).toList());
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ProductDto> updateProduct(@RequestPart ProductUpdateRequest request,
                                             @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.ok(modelMapper.map(productService.updateProduct(request, file), ProductDto.class));
    }

    @DeleteMapping("/deleteProductById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> deleteProductById(@PathVariable String id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok().build();
    }
}
