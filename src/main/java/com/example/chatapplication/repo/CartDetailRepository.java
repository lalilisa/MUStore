package com.example.chatapplication.repo;

import com.example.chatapplication.domain.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Long> {
}
