package com.example.chatapplication.repo;

import com.example.chatapplication.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {

    List<OrderDetail> getOrderDetailByOrderId(Long orderId);
}
