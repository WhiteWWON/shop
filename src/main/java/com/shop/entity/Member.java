package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberInfoCheckFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity{

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }
    public void updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        this.name = memberFormDto.getName();
        this.email = memberFormDto.getEmail();
        this.password = passwordEncoder.encode(memberFormDto.getPassword());
        this.address = memberFormDto.getAddress();
    }
    public static void validationCheck(MemberFormDto memberFormDto, BindingResult bindingResult){
        if(!memberFormDto.getPassword().equals(memberFormDto.getPassword_chk())){
            FieldError fieldError = new FieldError("memberFormDto", "password_chk", "비밀번호가 일치하지 않습니다.");
            bindingResult.addError(fieldError);
        }
        if(memberFormDto.getAddress().length() > 255){
            FieldError fieldError = new FieldError("memberFormDto", "address", "주소의 길이는 255자 이하로 입력 바랍니다.");
            bindingResult.addError(fieldError);
        }
    }
    public static void pwdMatchCheck(MemberFormDto memberFormDto, MemberInfoCheckFormDto memberInfoCheckFormDto,
                                     BindingResult bindingResult, PasswordEncoder passwordEncoder) {
        if(!passwordEncoder.matches(memberInfoCheckFormDto.getPassword(), memberFormDto.getPassword())) {
            FieldError fieldError = new FieldError("memberInfoCheckForm", "password", "입력하신 비밀번호가 일치하지 않습니다.");
            bindingResult.addError(fieldError);
        }
    }

}