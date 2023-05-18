package com.example.chatapplication.controller;


import com.example.chatapplication.domain.Category;
import com.example.chatapplication.domain.Product;
import com.example.chatapplication.dto.query.QueryDto;
import com.example.chatapplication.dto.query.QueryProduct;
import com.example.chatapplication.dto.view.ProductView;
import com.example.chatapplication.repo.CategoryRepository;
import com.example.chatapplication.service.read.ProductQueryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductQueryService productQueryService;
    private final CategoryRepository categoryRepository;

    @GetMapping("{id}")
    public ProductView getDetailproduct(@PathVariable Long id){
        return productQueryService.findOnes(id);
    }

    @GetMapping("")
    public List<Product> getProduct(QueryProduct product){
        return productQueryService.getProduct(product);
    }

    @GetMapping("/query")
    public List<ProductView> getProduct(QueryDto product) throws JsonProcessingException {
        return  productQueryService.findsEntity(product).getResult().stream().map(this::convertToView).collect(Collectors.toList());
    }

    private ProductView convertToView(Product product){
        Category category=categoryRepository.findById(product.getCategoryId()).orElse(null);
        return ProductView.builder()
                .id(product.getId())
                .code(product.getCode())
                .categoryId(product.getCategoryId())
                .description(product.getDescription())
                .name(product.getName())
                .discount(product.getDiscount())
                .img(product.getImg())
                .price(product.getPrice())
                .view(product.getView())
                .quantity(product.getQuantity())
                .categoryName(category!=null? category.getName():null)
                .build();
    }
}
