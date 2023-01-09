package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class MemberOrderDto {

    private Long memberId; //회원 아이디
    private String email; // 회원 email
    private String name;  // 회원 이름
    private Long orderId; //주문 아이디
    private LocalDateTime orderDate; //주문 날짜
    private OrderStatus orderStatus; //주문 상태

    @QueryProjection
    public MemberOrderDto(Long memberId, String email, String name, Long orderId, LocalDateTime orderDate, OrderStatus orderStatus){
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }

}