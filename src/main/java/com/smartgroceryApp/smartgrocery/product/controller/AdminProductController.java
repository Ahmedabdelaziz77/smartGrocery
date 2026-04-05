package com.smartgroceryApp.smartgrocery.product.controller;

import com.smartgroceryApp.smartgrocery.common.dto.PageResponse;
import com.smartgroceryApp.smartgrocery.product.dto.*;
import com.smartgroceryApp.smartgrocery.product.service.ApprovedProductService;
import com.smartgroceryApp.smartgrocery.product.service.DashboardService;
import com.smartgroceryApp.smartgrocery.product.service.OpenFoodFactsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor

public class AdminProductController {

    private final OpenFoodFactsService openFoodFactsService;

    private final ApprovedProductService approvedProductService;
    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponseDto> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExternalProductResDto>> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "page must be at least 1") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "size must be at least 1")int size) {
        return ResponseEntity.ok(openFoodFactsService.searchProducts(query, page, size));
    }

    @GetMapping("/approved")
    public ResponseEntity<PageResponse<ApprovedProductResponseDto>> getApprovedProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page index must be at least 0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") int size) {
        return ResponseEntity.ok(approvedProductService.getAllApprovedProducts(name, category, page, size));
    }

    @PostMapping("/import")
    public ResponseEntity<ApprovedProductResponseDto> importSingleProduct(
            @Valid @RequestBody ImportProductRequestDto req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(approvedProductService.importSingle(req));
    }

    @PostMapping("/import/bulk")
    public ResponseEntity<List<ApprovedProductResponseDto>> importBulkProducts(
            @Valid @RequestBody BulkImportRequestDto req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(approvedProductService.importBulk(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApprovedProduct(@PathVariable Long id) {
        approvedProductService.softDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
