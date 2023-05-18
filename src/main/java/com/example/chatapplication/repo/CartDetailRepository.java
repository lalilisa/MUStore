package com.example.chatapplication.repo;

import com.example.chatapplication.domain.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Long> {

    CartDetail findByCartIdAndProductId(Long cartId,Long productId);

    @Modifying
    @Transactional
    void deleteCartDetailByProductIdInAndCartId(List<Long> ids,Long cartId);
}
