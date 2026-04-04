package com.smartgroceryApp.smartgrocery.product.repository;

import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface ApprovedProductRepository extends JpaRepository<ApprovedProduct, Long> {
    boolean existsByExternalIdAndExternalSource(String externalId, String externalSource);
    Optional<ApprovedProduct> findByIdAndActiveTrue(Long id);
    Page<ApprovedProduct> findAllByActiveTrue(Pageable pageable);
}
