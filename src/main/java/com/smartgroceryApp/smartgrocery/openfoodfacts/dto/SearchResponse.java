package com.smartgroceryApp.smartgrocery.openfoodfacts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data @AllArgsConstructor @NoArgsConstructor
public class SearchResponse {
    private List<ProductDto> products;

    private Integer page;
    private Integer page_count;
    private Integer page_size;
    private Integer count;
}
