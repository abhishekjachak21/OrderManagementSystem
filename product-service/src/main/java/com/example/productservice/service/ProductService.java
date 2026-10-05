package com.example.productservice.service;


import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(CreateProductRequest request){

        Product product = new Product();

        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());

        Product saved = productRepository.save(product);

        return toResponse(saved);
    }

    public ProductResponse getProduct(Long id){

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return toResponse(product);

    }

    @Transactional
    public ProductResponse reduceStock(Long id, Integer quantity) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        product.setStock(product.getStock() - quantity);

        Product updated = productRepository.save(product);

        return toResponse(updated);
    }

    public boolean isAvailable(Long id, Integer quantity) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        return product.getStock() >= quantity;
    }


    private ProductResponse toResponse(Product product){

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }
}