package org.example.backend.controller;

import org.example.backend.controller.dto.ProductDTO;
import org.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<?> getMainPageList() {
        List<ProductDTO> productDTOS = productService.getRecentProducts();
        return ResponseEntity.ok(productDTOS);
    }
}
