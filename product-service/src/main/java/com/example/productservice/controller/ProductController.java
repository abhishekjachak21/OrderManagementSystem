package com.example.productservice.controller;

import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.dto.StockUpdateRequest;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id){

        return productService.getProduct(id);

    }

    @PutMapping("/{id}/stock")
    public ProductResponse reduceStock(
            @PathVariable Long id, @RequestBody StockUpdateRequest request) {

        return productService.reduceStock(id, request.quantity());
    }


    @GetMapping("/{id}/availability")
    public boolean checkAvailability(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        return productService.isAvailable(id, quantity);
    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody CreateProductRequest request){

        return productService.createProduct(request);

    }


}