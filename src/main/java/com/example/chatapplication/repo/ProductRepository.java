package com.example.chatapplication.repo;

import com.example.chatapplication.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {


    @Query(value = "select p from Product p inner join CartDetail cd " +
            "on p.id = cd.productId where cd.cartId = :cartId order by cd.createdAt asc ")
    List<Product> getItemInMyCart(Long cartId);


}
