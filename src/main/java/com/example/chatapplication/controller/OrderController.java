package com.example.chatapplication.controller;


import com.example.chatapplication.dto.request.UpdateCart;
import com.example.chatapplication.dto.view.MyOrders;
import com.example.chatapplication.dto.view.ProductView;
import com.example.chatapplication.service.read.OrderQueryService;
import com.example.chatapplication.service.write.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;
    @GetMapping("my-order")
    public List<MyOrders> getMyOrder(Authentication authentication)
            {
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
        return orderQueryService.getMyOrder(userDetails.getUsername());
    }

    @PostMapping("create-order")
    public MyOrders createMyOrder(Authentication authentication,@RequestBody List<ProductView> orderDetail
            ,@RequestParam("address") String address){
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
        return orderCommandService.createOrder(userDetails.getUsername(),orderDetail,address);
    }
}
