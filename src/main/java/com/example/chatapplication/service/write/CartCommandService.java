package com.example.chatapplication.service.write;

import com.example.chatapplication.common.Category;
import com.example.chatapplication.domain.*;
import com.example.chatapplication.dto.request.Notice;
import com.example.chatapplication.dto.response.ResponseMessage;
import com.example.chatapplication.exception.GeneralException;
import com.example.chatapplication.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@RequiredArgsConstructor
public class CartCommandService {

    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final CartDetailRepository cartDetailRepository;

    private final FireBaseNotifiCommandService fireBaseNotifiCommandService;
    private final TokenDeviceRepoditory tokenDeviceRepoditory;

    private final NotificationsRepository notificationsRepository;

    public CartDetail addSingleProduct(String username,Long productId,Integer quantity,Integer status){
        User user=userRepository.findByUsername(username);
        Long userId =user.getId();
        Cart cart=cartRepository.findByUserId(userId).orElse(null);

        if(cart==null){
            throw  new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Cart is not exist");
        }
        Product product=productRepository.findById(productId).orElseThrow(() ->
                new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Product is not exist")
                );
        if(product.getQuantity()<quantity)
            throw new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Product is not enough");
        CartDetail cartDetail=cartDetailRepository.findByCartIdAndProductId(cart.getId(),productId);
        if(cartDetail==null){
            cartDetail=CartDetail.builder()
                    .cartId(cart.getId())
                    .productId(productId)
                    .quantity(quantity)
                    .build();

        }
        else {
            cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
        }
        if(status==1)
            asyncNotifi(user,product.getImg());
        return cartDetailRepository.save(cartDetail);
    }
    public CartDetail decrementProductInCart(String username,Long productId,Integer quantity){
        User user=userRepository.findByUsername(username);
        Long userId =user.getId();
        Cart cart=cartRepository.findByUserId(userId).orElse(null);
        if(cart==null){
            throw  new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Cart is not exist");
        }

        CartDetail cartDetail=cartDetailRepository.findByCartIdAndProductId(cart.getId(),productId);
        if(cartDetail==null){
            throw new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Product is not in cart");
        }
        if(quantity>cartDetail.getQuantity())
            throw new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name()," greater quantity in cart");
        cartDetail.setQuantity(cartDetail.getQuantity()-quantity);
        return cartDetailRepository.save(cartDetail);
    }
    public ResponseMessage removeItemInCart(String username,Long productId){
        User user=userRepository.findByUsername(username);
        Long userId =user.getId();
        Cart cart=cartRepository.findByUserId(userId).orElse(null);
        if(cart==null){
            throw  new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Cart is not exist");
        }
        CartDetail cartDetail=cartDetailRepository.findByCartIdAndProductId(cart.getId(),productId);
        if(cartDetail==null){
            throw new GeneralException(Category.ErrorCodeEnum.INVALID_PARAMETER.name(),"Product is not in cart");
        }
        try {
            cartDetailRepository.deleteById(cartDetail.getId());
            return ResponseMessage.builder()
                    .message("SUCCESS")
                    .status(1)
                    .build();
        }
        catch (Exception exception){
            return ResponseMessage.builder()
                    .message("FAIL")
                    .status(0)
                    .build();
        }
    }
    private void sendNotifi(User user, String img){
        DeviceToken deviceToken = tokenDeviceRepoditory.findByUserId(user.getId());
        Map<String,String> map=new HashMap<>();
        map.put("type","ADD_TO_CART");
        Notice notice=Notice.builder()
                .subject("Thêm vào giỏ hàng thành công")
                .content("Sản phẩm đã được thêm vào giỏ hàng check ngay.")
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
                .type("CART")
                .build());
        fireBaseNotifiCommandService.sendNotification(notice);
    }
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private void asyncNotifi(User user,String img){
        CompletableFuture.runAsync(() -> {
            try {
                // Wait for 5 seconds

                Thread.sleep(2000);
                sendNotifi(user,img);

            } catch (InterruptedException e) {
                // Handle the exception

            }
        }, executorService);
    }
}
