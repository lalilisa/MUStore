package com.example.chatapplication.service.read;

import com.example.chatapplication.domain.Product;
import com.example.chatapplication.dto.query.QueryProduct;
import com.example.chatapplication.dto.view.ProductView;
import com.example.chatapplication.repo.CategoryRepository;
import com.example.chatapplication.repo.ProductRepository;
import com.example.chatapplication.service.impl.AbstractJpaDAO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class ProductQueryService extends AbstractJpaDAO<Product> {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    public ProductQueryService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        super(Product.class);
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    public ProductView findOnes(long id){
        Product product= productRepository.findById(id).orElse(null);
        if(product!=null)
            product.setView(product.getView()+1);
        return this.convertToView(productRepository.save(product));
    }


    public List<Product> getProduct(QueryProduct product){
        Sort sort= Sort.by(ordersField(product.getSort()));
        Pageable pageable= PageRequest.of(product.getPage(),product.getLimit(),sort);
        return productRepository.findAll(pageable).getContent();
    }

    private List<Sort.Order> ordersField(String sort){
        List<Sort.Order> orders=new ArrayList<>();
        String [] splitSort=sort.split(",+");
        for(String o:splitSort){
            String[] keyAndDerection=o.split(":");
            String key=keyAndDerection[0];
            String derection=keyAndDerection[1];
            if(derection.equals(Sort.Direction.ASC.name()))
                    orders.add(Sort.Order.asc(key));
            else
                orders.add(Sort.Order.desc(key));
        }

        return orders;
    }

    private ProductView convertToView(Product product){
        com.example.chatapplication.domain.Category category=categoryRepository.findById(product.getCategoryId()).orElse(null);
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
