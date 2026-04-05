package com.smartgroceryApp.smartgrocery.service;


import com.smartgroceryApp.smartgrocery.common.dto.PageResponse;
import com.smartgroceryApp.smartgrocery.common.exception.ResourceNotFoundException;
import com.smartgroceryApp.smartgrocery.product.dto.ProductDetailsResponseDto;
import com.smartgroceryApp.smartgrocery.product.dto.ProductSearchRequestDto;
import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import com.smartgroceryApp.smartgrocery.product.mapper.ApprovedProductMapper;
import com.smartgroceryApp.smartgrocery.product.repository.ApprovedProductRepository;
import com.smartgroceryApp.smartgrocery.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock private ApprovedProductRepository approvedProductRepository;
    @Mock private ApprovedProductMapper approvedProductMapper;

    @InjectMocks
    private ProductService productService;

    private ApprovedProduct testProduct;
    private ProductDetailsResponseDto testResponse;

    @BeforeEach
    void setUp() {
        testProduct = new ApprovedProduct();
        testProduct.setId(1L);
        testProduct.setName("Product");
        testProduct.setBrand("Brand");
        testProduct.setCategory("Category");
        testProduct.setCalories(379.0);
        testProduct.setProtein(13.0);
        testProduct.setCarbs(67.0);
        testProduct.setFat(7.0);
        testProduct.setEstimatedPrice(BigDecimal.valueOf(4.99));
        testProduct.setActive(true);

        testResponse = new ProductDetailsResponseDto(
                1L, "Product", "Brand", "Category",
                "Description", null, 379.0, 13.0, 67.0, 7.0,
                BigDecimal.valueOf(4.99)
        );
    }

    @Test
    @DisplayName("getAllProducts should return paginated response")
    void getAllProducts_shouldReturnPagedResponse() {
        Page<ApprovedProduct> page = new PageImpl<>(
                List.of(testProduct),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
                1
        );

        when(approvedProductRepository.findAllByActiveTrue(any(Pageable.class))).thenReturn(page);
        when(approvedProductMapper.toDetailsResponse(testProduct)).thenReturn(testResponse);

        PageResponse<ProductDetailsResponseDto> res = productService.getAllProducts(0, 10);

        assertThat(res.getContent()).hasSize(1);
        assertThat(res.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("getProductById should return product details")
    void getProductById_existingProduct_shouldReturnDetails() {
        when(approvedProductRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(testProduct));
        when(approvedProductMapper.toDetailsResponse(testProduct)).thenReturn(testResponse);

        ProductDetailsResponseDto res = productService.getProductById(1L);

        assertThat(res.getName()).isEqualTo("Product");
        assertThat(res.getCalories()).isEqualTo(379.0);
        assertThat(res.getEstimatedPrice()).isEqualTo(BigDecimal.valueOf(4.99));
    }

    @Test
    @DisplayName("getProductById should throw ResourceNotFoundException for missing product")
    void getProductById_nonExistentProduct_shouldThrow() {
        when(approvedProductRepository.findByIdAndActiveTrue(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("searchProducts should filter by name and category")
    void searchProducts_shouldReturnFilteredResults() {
        Page<ApprovedProduct> page = new PageImpl<>(
                List.of(testProduct),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
                1
        );

        when(approvedProductRepository.searchProducts(any(), any(), any(Pageable.class))).thenReturn(page);
        when(approvedProductMapper.toDetailsResponse(testProduct)).thenReturn(testResponse);

        ProductSearchRequestDto req = new ProductSearchRequestDto("pro", "Cat");
        PageResponse<ProductDetailsResponseDto> res = productService.searchProducts(req, 0, 10);

        assertThat(res.getContent()).hasSize(1);
        assertThat(res.getContent().get(0).getCategory()).isEqualTo("Category");
    }

    @Test
    @DisplayName("getCategories should return distinct category names")
    void getCategories_shouldReturnDistinctCategories() {
        when(approvedProductRepository.findDistinctCategories())
                .thenReturn(List.of("Category1", "Category2", "Category3"));

        List<String> categories = productService.getCategories();

        assertThat(categories).hasSize(3);
        assertThat(categories).containsExactly("Category1", "Category2", "Category3");
    }
}
