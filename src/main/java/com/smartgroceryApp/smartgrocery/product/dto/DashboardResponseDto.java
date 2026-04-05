package com.smartgroceryApp.smartgrocery.product.dto;

import java.util.List;

public record DashboardResponseDto(
        long totalProducts,
        long totalCategories,
        long totalUsers,
        List<RecentProductResponseDto> recentlyApproved,
        List<CategoryCountResponseDto> productsByCategory
) {
    public record RecentProductResponseDto(
            Long id,
            String name,
            String category,
            String imageUrl,
            String estimatedPrice
    ) {
    }

    public record CategoryCountResponseDto(
            String category,
            long count
    ) {
    }
}
