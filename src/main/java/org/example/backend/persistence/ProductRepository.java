package org.example.backend.persistence;

import org.example.backend.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    public Collection<ProductEntity> findByProductIdGreaterThanOrderByProductIdDesc(long productId);
}
