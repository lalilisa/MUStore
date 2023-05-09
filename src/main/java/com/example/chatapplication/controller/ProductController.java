package com.example.chatapplication.controller;


import com.example.chatapplication.domain.Product;
import com.example.chatapplication.dto.query.QueryDto;
import com.example.chatapplication.dto.query.QueryProduct;
import com.example.chatapplication.service.read.ProductQueryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductQueryService productQueryService;

    @GetMapping("{id}")
    public Product getDetailproduct(@PathVariable Long id){
        return productQueryService.findOne(id);
    }

    @GetMapping("")
    public List<Product> getProduct(QueryProduct product){
        return productQueryService.getProduct(product);
    }
    @GetMapping("/query")
    public List<Product> getProduct(QueryDto product) throws JsonProcessingException {
        return  productQueryService.findsEntity(product).getResult();
    }
}
