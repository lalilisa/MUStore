package com.example.chatapplication.repo;

import com.example.chatapplication.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {

    List<OrderDetail> getOrderDetailByOrderId(Long orderId);



    @Query(value = "SELECT od.* ,SUM(quantity) AS 'totalQuantity',(single_price*SUM(quantity)) AS 'totalPrice'  FROM order_detail od INNER JOIN orders o ON o.id=od.order_id \n" +
            " WHERE MONTH(od.created_at)=:target \n" +
            " GROUP BY od.product_id ",nativeQuery = true)
    List<OrderDetailStatic> getProductOrderInCurrnetMonth(@Param("target") Integer target);


    @Query(value = "SELECT a.category_id,SUM(quantity) AS 'totalQuantity',(single_price*SUM(quantity)) AS 'totalPrice' FROM (        \n" +
            "\tSELECT od.* ,SUM(quantity) AS 'totalQuantitys',(single_price*SUM(quantity)) AS 'totalPrices'  FROM order_detail od INNER JOIN orders o ON o.id=od.order_id \n" +
            "\t\tWHERE MONTH(od.created_at)=:month \n" +
            "\t\t\tGROUP BY od.product_id ) a\n" +
            "\t\t\tGROUP BY category_id",nativeQuery = true)
    List<CategoryStatic> getCategoryChart(@Param("month")Integer month);

    interface CategoryStatic{
        Long getCategory_id();
        Integer getTotalQuantity();
        Double getTotalPrice();;
    }
    interface OrderDetailStatic{

        Long getOrder_id();
        Double getSingle_price();
        Integer getQuantity();
        Long getCategory_id();
        String getProduct_name();
        Integer getTotalQuantity();
        Double getTotalPrice();
        Long getProduct_id();
    }
}
