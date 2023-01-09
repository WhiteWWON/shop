package com.shop.dto;

import com.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class MemberSearchFormDto {

    private Long id;

    private String email;
    private static ModelMapper modelMapper = new ModelMapper();
    public static MemberSearchFormDto of(Member member){
        return modelMapper.map(member, MemberSearchFormDto.class);
    }
}
