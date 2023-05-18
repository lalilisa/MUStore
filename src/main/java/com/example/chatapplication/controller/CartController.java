package com.example.chatapplication.controller;

import com.example.chatapplication.domain.CartDetail;
import com.example.chatapplication.dto.request.UpdateCart;
import com.example.chatapplication.dto.response.ResponseMessage;
import com.example.chatapplication.dto.view.ProductView;
import com.example.chatapplication.repo.CartRepository;
import com.example.chatapplication.repo.UserRepository;
import com.example.chatapplication.service.read.CartQueryService;
import com.example.chatapplication.service.read.UserQueryService;
import com.example.chatapplication.service.write.CartCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cart")
public class CartController {

    private final CartCommandService cartCommandService;
    private final CartQueryService cartQueryService;
    @GetMapping("all")
    public Object getMyCart(Authentication authentication){
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
        return cartQueryService.getMyCart(userDetails.getUsername());
    }

    @PostMapping("add-to-cart")
    public CartDetail addToMyCart(Authentication authentication,@RequestBody  UpdateCart cart,@RequestParam("status") Integer status){
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
        return cartCommandService.addSingleProduct(userDetails.getUsername(),cart.getProductId(),cart.getQuantity(),status);
    }

    @PutMapping("decrement-item")
    public CartDetail decrementToMyCart(Authentication authentication,@RequestBody  UpdateCart cart){
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
        return cartCommandService.decrementProductInCart(userDetails.getUsername(),cart.getProductId(),cart.getQuantity());
    }

    @DeleteMapping("remove-item")
    public ResponseMessage removeItemInCart(Authentication authentication,
                                            @RequestParam("productId") Long productId
                                            ){
        System.out.println(productId);
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
        return cartCommandService.removeItemInCart(userDetails.getUsername(),productId);
    }
    private final CartRepository cartRepository;
    @GetMapping("")
    public Object getMyCart(){

        return cartRepository.findAll();
    }
}
