package com.example.chatapplication.service.write;


import com.example.chatapplication.common.Category;
import com.example.chatapplication.domain.*;
import com.example.chatapplication.dto.request.Notice;
import com.example.chatapplication.dto.request.UpdateCart;
import com.example.chatapplication.dto.response.ResponseMessage;
import com.example.chatapplication.dto.view.MyOrders;
import com.example.chatapplication.dto.view.ProductView;
import com.example.chatapplication.exception.GeneralException;
import com.example.chatapplication.repo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final TokenDeviceRepoditory tokenDeviceRepoditory;
    private final FireBaseNotifiCommandService fireBaseNotifiCommandService;
    private final CartDetailRepository cartDetailRepository;
    private final NotificationsRepository notificationsRepository;
    @Transactional
    public MyOrders createOrder(String username, List<ProductView> orderDetail,String address){
        User user=userRepository.findByUsername(username);
        Long userId =user.getId();
        Cart cart=cartRepository.findByUserId(userId).orElse(null);
        if(cart==null){
            throw  new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Cart is not exist");
        }
        Double total= orderDetail.stream().mapToDouble(value -> value.getPrice()-(1-value.getDiscount()/100)).sum();
        Order order=orderRepository.save(Order.builder().address(address).cartId(cart.getId()).orderCode(UUID.randomUUID().toString()).total(total).status(0).build());
        cartDetailRepository.deleteCartDetailByProductIdInAndCartId(
                orderDetail.stream().map(ProductView::getId).collect(Collectors.toList()),cart.getId()
        );
        List<OrderDetail> orderDetails=orderDetail.stream().map(
                productView -> this.convertToOrderDetail(productView,order.getId())
        ).collect(Collectors.toList());
        orderDetailRepository.saveAll(orderDetails);
        asyncNotifi(user,orderDetails.get(0).getImg());
        return MyOrders.builder()
                .address(order.getAddress())
                .name(user.getFullname())
                .totalPrice(order.getTotal())
                .productViews(orderDetail)
                .build();
    }


    private OrderDetail convertToOrderDetail(ProductView view,Long orderId){
        return OrderDetail.builder()
                .orderId(orderId)
                .img(view.getImg())
                .price(view.getPrice())
                .productId(view.getId())
                .quantity(view.getQuantity())
                .build();
    }

//    public ResponseMessage removeOrder(Long orderId){
//        Order order=orderRepository.findById(orderId).orElse(null);
//        List<OrderDetail> orderDetails=orderDetailRepository.getOrderDetailByOrderId(orderId);
//
//        orderDetailRepository.deleteAll(orderDetails);
//        or
//
//    }

    private void sendNotifi(User user, String img){
            DeviceToken deviceToken = tokenDeviceRepoditory.findByUserId(user.getId());
            Map<String,String> map=new HashMap<>();
            map.put("type","ORDER");
            Notice notice=Notice.builder()
                    .subject("Đặt hàng thành công")
                    .content("Bạn đã đặt hàng thành công. Nhớ theo dõi để nhận hàng nhé")
                    .data(map)
                    .image(img)
                    .registrationTokens(new ArrayList<>() {{
                        add(deviceToken.getFcmToken());
                    }})
                    .build();
        notificationsRepository.save(Notifications.builder().content(
                notice.getContent())
                .img(img)
                .userId(user.getId())
                .mainRef(null)
                .title(notice.getSubject())
                .type("ORDER")
                .build());
            fireBaseNotifiCommandService.sendNotification(notice);
    }
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private void asyncNotifi(User user,String img){
        CompletableFuture.runAsync(() -> {
            try {
                // Wait for 5 seconds
                Thread.sleep(5000);
                sendNotifi(user,img);

            } catch (InterruptedException e) {
                // Handle the exception

            }
        }, executorService);
    }
}
