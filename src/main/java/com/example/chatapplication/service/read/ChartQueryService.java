package com.example.chatapplication.service.read;

import com.example.chatapplication.domain.Category;
import com.example.chatapplication.dto.response.DataCategoryChart;
import com.example.chatapplication.repo.CategoryRepository;
import com.example.chatapplication.repo.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChartQueryService {


    private final OrderDetailRepository orderDetailRepository;

    private final CategoryRepository categoryRepository;
    public List<OrderDetailRepository.OrderDetailStatic> getDataChart(Integer month){
       List<OrderDetailRepository.OrderDetailStatic> data= orderDetailRepository.getProductOrderInCurrnetMonth(month);
       data.stream().forEach(orderDetailStatic ->
               System.out.println(orderDetailStatic.getOrder_id()+" "+
                       orderDetailStatic.getCategory_id()+" "+ orderDetailStatic.getProduct_name()+ " "+
                       orderDetailStatic.getTotalPrice() + " "+orderDetailStatic.getTotalQuantity()
                        + " " + orderDetailStatic.getProduct_name()+" "+ orderDetailStatic.getProduct_id()
               )

               );
       return data;
    }


    public List<DataCategoryChart> getDataChartCategory(Integer month){
        List<OrderDetailRepository.CategoryStatic> data=orderDetailRepository.getCategoryChart(month);
        int totalSent=data.stream().mapToInt(OrderDetailRepository.CategoryStatic::getTotalQuantity).sum();
        double totalPriceSent=data.stream().mapToDouble(OrderDetailRepository.CategoryStatic::getTotalPrice).sum();
       return data.stream().map(categoryStatic -> {
            Category category=categoryRepository.findById(categoryStatic.getCategory_id()).orElse(null);
            return DataCategoryChart.builder()
                    .categoryId(category==null?1L:category.getId())
                    .categoryName(category.getName())
                    .totalPrice(categoryStatic.getTotalPrice())
                    .totalQuantity(categoryStatic.getTotalQuantity())
                    .percentQuantity((double)categoryStatic.getTotalQuantity()*100/totalSent)
                     .percentPrice(categoryStatic.getTotalPrice() *100/totalPriceSent)
                    .build();
        }).collect(Collectors.toList());
    }

}
