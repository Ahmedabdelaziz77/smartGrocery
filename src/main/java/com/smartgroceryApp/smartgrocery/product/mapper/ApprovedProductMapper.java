package com.smartgroceryApp.smartgrocery.product.mapper;

import com.smartgroceryApp.smartgrocery.product.dto.ApprovedProductResponseDto;
import com.smartgroceryApp.smartgrocery.product.dto.ProductDetailsResponseDto;
import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApprovedProductMapper {
    @Mapping(target = "externalSource", expression = "java(product.getExternalSource())")
    @Mapping(target = "active", source = "active")
    ApprovedProductResponseDto toResponse(ApprovedProduct product);

    ProductDetailsResponseDto toDetailsResponse(ApprovedProduct product);
}
