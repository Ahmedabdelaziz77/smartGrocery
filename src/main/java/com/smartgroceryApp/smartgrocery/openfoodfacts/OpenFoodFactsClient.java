package com.smartgroceryApp.smartgrocery.openfoodfacts;

import com.smartgroceryApp.smartgrocery.common.exception.BusinessException;
import com.smartgroceryApp.smartgrocery.openfoodfacts.dto.ProductDetailsResponseDto;
import com.smartgroceryApp.smartgrocery.openfoodfacts.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenFoodFactsClient {
    @Qualifier("openFoodFactsRestClient")
    private final RestClient restClient;

    public SearchResponse searchProducts(String query, int page, int size) {
        log.debug("searching open food facts external api");
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/cgi/search.pl")
                            .queryParam("search_terms", query)
                            .queryParam("search_simple", 1)
                            .queryParam("action", "process")
                            .queryParam("json", 1)
                            .queryParam("page", page)
                            .queryParam("page_size", size)
                            .build())
                    .retrieve()
                    .body(SearchResponse.class);
        } catch (RestClientException ex) {
            log.error("failed to search open food facts  error={}", ex.getMessage());
            throw new BusinessException("failed to search products");
        }
    }

    public ProductDetailsResponseDto getProductDetails(String code) {
        log.debug("fetching product details");
        try {
            return restClient.get()
                    .uri("/api/v0/product/{code}.json", code)
                    .retrieve()
                    .body(ProductDetailsResponseDto.class);
        } catch (RestClientException ex) {
            log.error("failed to fetch product details, error={}", ex.getMessage());
            throw new BusinessException("failed to fetch product details");
        }
    }
}
