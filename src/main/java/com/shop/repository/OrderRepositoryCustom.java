package com.shop.repository;

import com.shop.dto.MemberOrderDto;
import com.shop.dto.MemberSearchFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom{

    Page<MemberOrderDto> getMemberOrderPage(MemberSearchFormDto memberSearchFormDto, Pageable pageable);
}