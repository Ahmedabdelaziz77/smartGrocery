package com.smartgroceryApp.smartgrocery.product.controller;

import com.smartgroceryApp.smartgrocery.common.dto.PageResponse;
import com.smartgroceryApp.smartgrocery.product.dto.ProductDetailsResponseDto;
import com.smartgroceryApp.smartgrocery.product.dto.ProductSearchRequestDto;
import com.smartgroceryApp.smartgrocery.product.service.ProductService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageResponse<ProductDetailsResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page index must be at least 0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") int size
    ) {
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(productService.getCategories());
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductDetailsResponseDto>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page index must be at least 0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "size must be at least 1") int size
    ) {
        ProductSearchRequestDto request = new ProductSearchRequestDto(name, category);

        return ResponseEntity.ok(productService.searchProducts(request, page, size));
    }
}
