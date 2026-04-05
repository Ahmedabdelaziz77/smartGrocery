package com.smartgroceryApp.smartgrocery.product.service;

import com.smartgroceryApp.smartgrocery.common.dto.PageResponse;
import com.smartgroceryApp.smartgrocery.common.exception.DuplicationException;
import com.smartgroceryApp.smartgrocery.common.exception.ResourceNotFoundException;
import com.smartgroceryApp.smartgrocery.common.util.CurrentUserService;
import com.smartgroceryApp.smartgrocery.common.util.PaginationUtils;
import com.smartgroceryApp.smartgrocery.product.dto.ApprovedProductResponseDto;
import com.smartgroceryApp.smartgrocery.product.dto.BulkImportRequestDto;
import com.smartgroceryApp.smartgrocery.product.dto.ExternalProductResDto;
import com.smartgroceryApp.smartgrocery.product.dto.ImportProductRequestDto;
import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import com.smartgroceryApp.smartgrocery.product.mapper.ApprovedProductMapper;
import com.smartgroceryApp.smartgrocery.product.repository.ApprovedProductRepository;
import com.smartgroceryApp.smartgrocery.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovedProductService {

    private final ApprovedProductRepository approvedProductRepository;
    private final OpenFoodFactsService openFoodFactsService;
    private final ApprovedProductMapper approvedProductMapper;

    private final CurrentUserService currentUserService;

    public ApprovedProductResponseDto importSingle(ImportProductRequestDto request) {

        Optional<ApprovedProduct> existing = approvedProductRepository.findByExternalIdAndExternalSource(
                request.getExternalId(),
                "OPEN_FOOD_FACTS");

        if (existing.isPresent() && existing.get().isActive()) {
            throw new DuplicationException("product already imported !!");
        }

        ExternalProductResDto externalProd = openFoodFactsService.getProductDetails(request.getExternalId());

        User admin = currentUserService.getCurrentUser();

        // reactivate the soft deleted product or create a new one
        ApprovedProduct approvedProduct = existing.orElseGet(ApprovedProduct::new);

        approvedProduct.setExternalId(externalProd.getExternalId());
        approvedProduct.setExternalSource("OPEN_FOOD_FACTS");
        approvedProduct.setName(externalProd.getName());
        approvedProduct.setBrand(externalProd.getBrand());
        approvedProduct.setCategory(externalProd.getCategory());
        approvedProduct.setDescription(externalProd.getDescription());
        approvedProduct.setImageUrl(externalProd.getImageUrl());
        approvedProduct.setCalories(externalProd.getCalories());
        approvedProduct.setProtein(externalProd.getProtein());
        approvedProduct.setCarbs(externalProd.getCarbs());
        approvedProduct.setFat(externalProd.getFat());
        approvedProduct.setEstimatedPrice(request.getEstimatedPrice());
        approvedProduct.setApprovedBy(admin);
        approvedProduct.setActive(true);

        ApprovedProduct saved = approvedProductRepository.save(approvedProduct);

        log.info("imported product with id={}, externalId={}, name='{}'", saved.getId(), saved.getExternalId(),
                saved.getName());

        return approvedProductMapper.toResponse(saved);
    }

    public List<ApprovedProductResponseDto> importBulk(BulkImportRequestDto request) {
        List<ApprovedProductResponseDto> res = new ArrayList<>();

        for (ImportProductRequestDto productRequest : request.getProducts()) {

            res.add(importSingle(productRequest));
        }

        log.info("bulk import completed!");
        return res;
    }

    public PageResponse<ApprovedProductResponseDto> getAllApprovedProducts(int page, int size) {
        Pageable pageable = PaginationUtils.createPageRequest(page, size, "createdAt");
        Page<ApprovedProduct> productPage = approvedProductRepository.findAllByActiveTrue(pageable);
        return PaginationUtils.toPageResponse(productPage, approvedProductMapper::toResponse);
    }

    @Transactional
    public void softDeleteProduct(Long id) {
        ApprovedProduct product = approvedProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("appproved product not found with id -> " + id));

        product.setActive(false);
        approvedProductRepository.save(product);
        log.info("Soft-deleted product: id={}, name='{}'", id, product.getName());
    }

}
