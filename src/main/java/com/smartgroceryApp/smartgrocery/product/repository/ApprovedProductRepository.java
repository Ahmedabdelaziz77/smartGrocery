package com.smartgroceryApp.smartgrocery.product.repository;

import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApprovedProductRepository extends JpaRepository<ApprovedProduct, Long> {
    boolean existsByExternalIdAndExternalSource(String externalId, String externalSource);
    Optional<ApprovedProduct> findByIdAndActiveTrue(Long id);
    Page<ApprovedProduct> findAllByActiveTrue(Pageable pageable);



    @Query("""
    SELECT p FROM ApprovedProduct p
    WHERE p.active = true
    AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
    AND (:category IS NULL OR LOWER(p.category) LIKE LOWER(CONCAT('%', :category, '%')))
""")
    Page<ApprovedProduct> searchProducts(
            @Param("name") String name,
            @Param("category") String category,
            Pageable pageable
    );


    @Query("""
    SELECT DISTINCT p.category FROM ApprovedProduct p
    WHERE p.active = true AND p.category IS NOT NULL
    ORDER BY p.category
    """)
    List<String> findDistinctCategories();

    long countByActiveTrue();

    @Query("SELECT COUNT(DISTINCT p.category) FROM ApprovedProduct p WHERE p.active = true AND p.category IS NOT NULL")
    long countDistinctCategories();


    List<ApprovedProduct> findTop5ByActiveTrueOrderByCreatedAtDesc();
    @Query("SELECT p.category, COUNT(p) FROM ApprovedProduct p WHERE p.active = true AND p.category IS NOT NULL GROUP BY p.category ORDER BY COUNT(p) DESC")
    List<Object[]> countByCategory();
}
