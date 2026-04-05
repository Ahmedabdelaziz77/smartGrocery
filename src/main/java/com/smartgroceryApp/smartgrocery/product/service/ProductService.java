package com.smartgroceryApp.smartgrocery.product.service;


import com.smartgroceryApp.smartgrocery.common.dto.PageResponse;
import com.smartgroceryApp.smartgrocery.common.exception.ResourceNotFoundException;
import com.smartgroceryApp.smartgrocery.common.util.PaginationUtils;
import com.smartgroceryApp.smartgrocery.product.dto.ProductDetailsResponseDto;
import com.smartgroceryApp.smartgrocery.product.dto.ProductSearchRequestDto;
import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import com.smartgroceryApp.smartgrocery.product.mapper.ApprovedProductMapper;
import com.smartgroceryApp.smartgrocery.product.repository.ApprovedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

@RequiredArgsConstructor
public class ProductService {
    private final ApprovedProductRepository approvedProductRepository;
    private final ApprovedProductMapper approvedProductMapper;

    public PageResponse<ProductDetailsResponseDto> getAllProducts(int page, int size) {
        Pageable pageable = PaginationUtils.createPageRequest(page, size, "createdAt");
        Page<ApprovedProduct> productPage = approvedProductRepository.findAllByActiveTrue(pageable);
        return PaginationUtils.toPageResponse(productPage, approvedProductMapper::toDetailsResponse);
    }

    public ProductDetailsResponseDto getProductById(Long id) {
        ApprovedProduct product = approvedProductRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("product not found with id --> " + id));

        return approvedProductMapper.toDetailsResponse(product);
    }

    public PageResponse<ProductDetailsResponseDto> searchProducts(ProductSearchRequestDto req, int page, int size) {
        String name = req.getName() != null ? req.getName().trim() : "";
        String category = req.getCategory() != null ? req.category.trim() : "";

        Pageable pageable = PaginationUtils.createPageRequest(page, size, "createdAt");
        Page<ApprovedProduct> productPage = approvedProductRepository.searchProducts(name, category, pageable);

        return PaginationUtils.toPageResponse(productPage, approvedProductMapper::toDetailsResponse);
    }

    public List<String> getCategories() {
        return approvedProductRepository.findDistinctCategories();
    }


}
