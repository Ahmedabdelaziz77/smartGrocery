package com.smartgroceryApp.smartgrocery.service;

import com.smartgroceryApp.smartgrocery.product.dto.DashboardResponseDto;
import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import com.smartgroceryApp.smartgrocery.product.repository.ApprovedProductRepository;
import com.smartgroceryApp.smartgrocery.product.service.DashboardService;
import com.smartgroceryApp.smartgrocery.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {
    @Mock private ApprovedProductRepository approvedProductRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    @DisplayName("getDashboard should return all stats and lists")
    void getDashboard_shouldReturnCompleteResponse() {

        ApprovedProduct prod = new ApprovedProduct();
        prod.setId(1L);
        prod.setName("Product1");
        prod.setCategory("Category1");
        prod.setEstimatedPrice(BigDecimal.valueOf(4.99));

        when(approvedProductRepository.countByActiveTrue()).thenReturn(20L);
        when(approvedProductRepository.countDistinctCategories()).thenReturn(11L);
        when(userRepository.count()).thenReturn(2L);
        when(approvedProductRepository.findTop5ByActiveTrueOrderByCreatedAtDesc())
                .thenReturn(List.of(prod));
        when(approvedProductRepository.countByCategory())
                .thenReturn(List.of(
                        new Object[]{"Category1", 4L},
                        new Object[]{"Category2", 2L}
                ));

        DashboardResponseDto result = dashboardService.getDashboard();

        assertThat(result.totalProducts()).isEqualTo(20L);
        assertThat(result.totalCategories()).isEqualTo(11L);
        assertThat(result.totalUsers()).isEqualTo(2L);
        assertThat(result.recentlyApproved()).hasSize(1);
        assertThat(result.recentlyApproved().getFirst().name()).isEqualTo("Product1");
        assertThat(result.productsByCategory()).hasSize(2);
        assertThat(result.productsByCategory().getFirst().category()).isEqualTo("Category1");
        assertThat(result.productsByCategory().getFirst().count()).isEqualTo(4L);
    }

    @Test
    @DisplayName("getDashboard should handle empty database")
    void getDashboard_emptyDatabase_shouldReturnZeros() {
        when(approvedProductRepository.countByActiveTrue()).thenReturn(0L);
        when(approvedProductRepository.countDistinctCategories()).thenReturn(0L);
        when(userRepository.count()).thenReturn(0L);
        when(approvedProductRepository.findTop5ByActiveTrueOrderByCreatedAtDesc())
                .thenReturn(Collections.emptyList());
        when(approvedProductRepository.countByCategory())
                .thenReturn(Collections.emptyList());

        DashboardResponseDto res = dashboardService.getDashboard();

        assertThat(res.totalProducts()).isZero();
        assertThat(res.totalCategories()).isZero();
        assertThat(res.totalUsers()).isZero();
        assertThat(res.recentlyApproved()).isEmpty();
        assertThat(res.productsByCategory()).isEmpty();
    }

    @Test
    @DisplayName("getDashboard should format price with dollar sign")
    void getDashboard_shouldFormatPrice() {
        ApprovedProduct prod = new ApprovedProduct();
        prod.setId(1L);
        prod.setName("Test");
        prod.setCategory("Test Category");
        prod.setEstimatedPrice(BigDecimal.valueOf(5.49));

        when(approvedProductRepository.countByActiveTrue()).thenReturn(1L);
        when(approvedProductRepository.countDistinctCategories()).thenReturn(1L);
        when(userRepository.count()).thenReturn(1L);
        when(approvedProductRepository.findTop5ByActiveTrueOrderByCreatedAtDesc())
                .thenReturn(List.of(prod));
        when(approvedProductRepository.countByCategory())
                .thenReturn(Collections.emptyList());

        DashboardResponseDto result = dashboardService.getDashboard();

        assertThat(result.recentlyApproved().getFirst().estimatedPrice()).isEqualTo("$5.49");
    }
}
