package com.example.chatapplication.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class Order extends Audiant{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code",unique = true)

    private String orderCode;
    @Column(name = "total")
    private Double total;

    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "address")
    private String address;

}
