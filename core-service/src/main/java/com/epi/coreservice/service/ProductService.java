package com.epi.coreservice.service;

import com.epi.coreservice.exc.NotFoundException;
import com.epi.coreservice.model.Category;
import com.epi.coreservice.model.Product;
import com.epi.coreservice.repository.ProductRepository;
import com.epi.coreservice.request.product.ProductUpdateRequest;
import com.epi.coreservice.client.FileStorageClient;
import com.epi.coreservice.request.product.ProductCreateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final FileStorageClient fileStorageClient;
    private final ModelMapper modelMapper;

    public Product createProduct(ProductCreateRequest request, MultipartFile file) {
        Category category = categoryService.getCategoryById(request.getCategoryId());

        String imageId = null;

        if (file != null)
            imageId = fileStorageClient.uploadImageToFIleSystem(file).getBody();

        return productRepository.save(Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(category)
                .imageId(imageId)
                .build());
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getProductById(String id) {
        return findProductById(id);
    }

    public Product updateProduct(ProductUpdateRequest request, MultipartFile file) {
        Product toUpdate = findProductById(request.getId());

        if (file != null) {
            String imageId = fileStorageClient.uploadImageToFIleSystem(file).getBody();
            if (imageId != null) {
                fileStorageClient.deleteImageFromFileSystem(toUpdate.getImageId());
                toUpdate.setImageId(imageId);
            }
        }

        return productRepository.save(toUpdate);
    }

    public void deleteProductById(String id) {
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByCategoryId(String id) {
        return productRepository.getProductsByCategoryId(id);
    }

    public List<Product> getProductsThatFitYourNeeds(String needs) {
        String[] keys = needs.replaceAll("\"", "").split(" ");
        HashMap<String, Integer> map = new HashMap<>();
        Arrays.stream(keys).forEach(key -> productRepository.getProductsByKeysContainsIgnoreCase(key)
                .forEach(job -> {
                    if (map.containsKey(job.getId())) {
                        int count = map.get(job.getId());
                        map.put(job.getId(), count + 1);
                    } else map.put(job.getId(), 1);
                }));
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> findProductById(entry.getKey()))
                .collect(Collectors.toList());
    }

    protected Product findProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }
}
