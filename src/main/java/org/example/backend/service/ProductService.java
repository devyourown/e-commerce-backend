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

    public ProductDTO getProductById(Long id) {
        ProductEntity entity = productRepository.findByProductId(id);
        return productEntityToDTO(entity);
    }

    private ProductDTO productEntityToDTO(ProductEntity entity) {
        return ProductDTO.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .title(entity.getTitle())
                .imageUrl(entity.getImageUrl())
                .price(entity.getPrice())
                .category(entity.getCategory())
                .content(entity.getContent())
                .recommend(entity.getRecommend())
                .build();
    }

    private List<ProductDTO> productEntitiesToDTOS(List<ProductEntity> entities) {
        List<ProductDTO> result = new ArrayList<>();
        for (ProductEntity entity : entities) {
            result.add(productEntityToDTO(entity));
        }
        return result;
    }
}
