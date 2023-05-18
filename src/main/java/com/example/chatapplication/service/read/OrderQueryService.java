package com.example.chatapplication.service.read;


import com.example.chatapplication.common.Category;
import com.example.chatapplication.domain.*;
import com.example.chatapplication.dto.view.MyOrders;
import com.example.chatapplication.dto.view.ProductView;
import com.example.chatapplication.exception.GeneralException;
import com.example.chatapplication.repo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderQueryService {

    private  final OrderRepository orderRepository;
    private  final OrderDetailRepository orderDetailRepository;
    private  final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;
    public List<MyOrders> getMyOrder(String username){
        User user=userRepository.findByUsername(username);
        Long userId =user.getId();
        Cart cart=cartRepository.findByUserId(userId).orElse(null);
        if(cart==null){
            throw  new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Cart is not exist");
        }
        List<Order> orders=orderRepository.findByCartId(cart.getId());

       return   orders.stream().map(order ->
               MyOrders.builder()
               .address(order.getAddress())
               .name(user.getFullname())
               .totalPrice(order.getTotal())
               .productViews(null)
               .build()).collect(Collectors.toList());
    }

}
