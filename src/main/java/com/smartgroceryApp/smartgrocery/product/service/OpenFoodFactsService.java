package com.smartgroceryApp.smartgrocery.product.service;

import com.smartgroceryApp.smartgrocery.common.exception.ResourceNotFoundException;
import com.smartgroceryApp.smartgrocery.openfoodfacts.OpenFoodFactsClient;
import com.smartgroceryApp.smartgrocery.openfoodfacts.dto.NutrimentsDto;
import com.smartgroceryApp.smartgrocery.openfoodfacts.dto.ProductDetailsResponseDto;
import com.smartgroceryApp.smartgrocery.openfoodfacts.dto.ProductDto;
import com.smartgroceryApp.smartgrocery.openfoodfacts.dto.SearchResponse;
import com.smartgroceryApp.smartgrocery.product.dto.ExternalProductResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenFoodFactsService {
    private final OpenFoodFactsClient openFoodFactsClient;

    public List<ExternalProductResDto> searchProducts(String query, int page, int size) {

        SearchResponse response = openFoodFactsClient.searchProducts(query, page, size);

        if (response == null || response.getProducts() == null) {
            return Collections.emptyList();
        }

        return response.getProducts().stream()
                .map(this::mapToResponse)
                .filter(product -> product.getExternalId() != null)
                .toList();
    }

    public ExternalProductResDto getProductDetails(String code) {
        ProductDetailsResponseDto response = openFoodFactsClient.getProductDetails(code);

        if (response == null || response.getProduct() == null || response.getStatus() == null || response.getStatus() != 1) {
            throw new ResourceNotFoundException("external product doesn't exist!!");
        }
        return mapToResponse(response.getProduct());
    }

    private ExternalProductResDto mapToResponse(ProductDto product) {

        NutrimentsDto nutriments = product.getNutriments();

        return new ExternalProductResDto(
                product.getCode() != null ? product.getCode().trim() : null,
                "OPEN_FOOD_FACTS",
                product.getProductName() != null ? product.getProductName().trim() : "Unknown",
                product.getBrands() != null ? product.getBrands().trim() : null,
                firstCategory(product.getCategories()),
                product.getGenericName() != null ? product.getGenericName().trim() : null,
                product.getImageUrl() != null ? product.getImageUrl().trim() : null,
                nutriments != null ? nutriments.getEnergyKcal100g() : null,
                nutriments != null ? nutriments.getProteins100g() : null,
                nutriments != null ? nutriments.getCarbohydrates100g() : null,
                nutriments != null ? nutriments.getFat100g() : null
        );
    }

    private String firstCategory(String categories) {
        if (categories == null || categories.isBlank()) {
            return null;
        }

        String[] parts = categories.split(",");
        if (parts.length == 0 || parts[0].isBlank()) {
            return null;
        }

        return parts[0].trim();
    }
}
