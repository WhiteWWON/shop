package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.dto.MemberOrderDto;
import com.shop.dto.MemberSearchFormDto;
import com.shop.dto.QMemberOrderDto;
import com.shop.entity.QMember;
import com.shop.entity.QOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public OrderRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MemberOrderDto> getMemberOrderPage(MemberSearchFormDto memberSearchFormDto, Pageable pageable) {
        QMember member = QMember.member;
        QOrder order = QOrder.order;

        QueryResults<MemberOrderDto> results = queryFactory
                .select(
                        new QMemberOrderDto(
                                member.id,
                                member.email,
                                member.name,
                                order.id,
                                order.orderDate,
                                order.orderStatus)
                )
                .from(order)
                .join(order.member, member)
                //.where(member.email.eq(memberSearchFormDto.getEmail()))
                .orderBy(member.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults()
                ;

        List<MemberOrderDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}