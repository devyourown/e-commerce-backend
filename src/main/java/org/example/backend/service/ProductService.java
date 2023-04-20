package org.example.backend.service;

import org.example.backend.controller.dto.ProductDTO;
import org.example.backend.persistence.ProductRepository;
import org.example.backend.persistence.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<ProductDTO> getRecentProducts() {
        List<ProductEntity> productEntities = productRepository.
                findByProductIdGreaterThanOrderByProductIdDesc(20).stream().toList();
        return productEntitiesToDTOS(productEntities);
    }

    private List<ProductDTO> productEntitiesToDTOS(List<ProductEntity> entities) {
        List<ProductDTO> result = new ArrayList<>();
        for (ProductEntity entity : entities) {
            result.add(ProductDTO.builder()
                    .id(entity.getId())
                    .productId(entity.getProductId())
                    .title(entity.getTitle())
                    .price(entity.getPrice())
                    .category(entity.getCategory())
                    .content(entity.getContent())
                    .recommend(entity.getRecommend())
                    .build());
        }
        return result;
    }
}
