package com.shop.dto;

import com.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberInfoCheckFormDto {

    private Long id;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호 확인을 위해 입력해 주세요.")
    @Length(min=8, max=20, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;

    private static ModelMapper modelMapper = new ModelMapper();
    public static MemberInfoCheckFormDto of(Member member){
        return modelMapper.map(member, MemberInfoCheckFormDto.class);
    }
}
