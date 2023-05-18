package com.example.chatapplication.service.read;

import com.example.chatapplication.common.Category;
import com.example.chatapplication.domain.Cart;
import com.example.chatapplication.domain.CartDetail;
import com.example.chatapplication.domain.Product;
import com.example.chatapplication.domain.User;
import com.example.chatapplication.dto.view.ProductView;
import com.example.chatapplication.exception.GeneralException;
import com.example.chatapplication.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartQueryService {

    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final CartDetailRepository cartDetailRepository;

    private final CategoryRepository categoryRepository;

    public List<ProductView> getMyCart(String username){
        User user=userRepository.findByUsername(username);
        if(user==null){
            throw  new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"User is not exist");
        }
        Cart cart=cartRepository.findByUserId(user.getId()).orElse(null);
        if(cart==null){
            throw  new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Cart is not exist");
        }
        List<Product> products=productRepository.getItemInMyCart(cart.getId());
        return products.stream().map(product -> {
            CartDetail cartDetail=cartDetailRepository.findByCartIdAndProductId(cart.getId(),product.getId());
            return this.convertToView(product,cartDetail.getQuantity());
        }).collect(Collectors.toList());
    }

    private ProductView  convertToView(Product product,Integer myQuantity){
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
                    .quantity(myQuantity)
                    .categoryName(category!=null? category.getName():null)
                    .build();
    }
}
