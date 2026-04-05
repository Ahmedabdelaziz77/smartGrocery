package com.smartgroceryApp.smartgrocery.product.service;

import com.smartgroceryApp.smartgrocery.product.dto.DashboardResponseDto;
import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import com.smartgroceryApp.smartgrocery.product.repository.ApprovedProductRepository;
import com.smartgroceryApp.smartgrocery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ApprovedProductRepository approvedProductRepository;
    private final UserRepository userRepository;

    public DashboardResponseDto getDashboard() {
        long totalProducts = approvedProductRepository.countByActiveTrue();
        long totalCategories = approvedProductRepository.countDistinctCategories();
        long totalUsers = userRepository.count();

        List<DashboardResponseDto.RecentProductResponseDto> recentlyApproved =
                approvedProductRepository.findTop5ByActiveTrueOrderByCreatedAtDesc()
                        .stream()
                        .map(this::toRecentProduct)
                        .toList();

        List<DashboardResponseDto.CategoryCountResponseDto> productsByCategory =
                approvedProductRepository.countByCategory()
                        .stream()
                        .map(row -> new DashboardResponseDto.CategoryCountResponseDto(
                                (String) row[0],
                                (Long) row[1]
                        ))
                        .toList();

        return new DashboardResponseDto(
                totalProducts,
                totalCategories,
                totalUsers,
                recentlyApproved,
                productsByCategory
        );
    }

    private DashboardResponseDto.RecentProductResponseDto toRecentProduct(ApprovedProduct product) {
        return new DashboardResponseDto.RecentProductResponseDto(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getImageUrl(),
                product.getEstimatedPrice() != null ? "$" + product.getEstimatedPrice().toPlainString() : null
        );
    }
}
