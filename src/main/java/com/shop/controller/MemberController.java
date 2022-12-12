package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberInfoCheckFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        Member.validationCheck(memberFormDto, bindingResult);
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/login";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/preModify")
    public String memberInfoCheck(Principal principal, Model model){

        MemberInfoCheckFormDto memberInfoCheckFormDto = new MemberInfoCheckFormDto();
        memberInfoCheckFormDto.setEmail(principal.getName());
        model.addAttribute("memberInfoCheckFormDto", memberInfoCheckFormDto);

        return "member/memberInfoCheckForm";
    }

    @GetMapping(value = "/modify")
    public String memberInfo(@Valid MemberInfoCheckFormDto memberInfoCheckFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/memberInfoCheckForm";
        }
        try{
            MemberFormDto memberFormDto = memberService.getMemberInfo(memberInfoCheckFormDto.getEmail());
            Member.pwdMatchCheck(memberFormDto, memberInfoCheckFormDto, bindingResult, passwordEncoder);
            if(bindingResult.hasErrors()){
                return "member/memberInfoCheckForm";
            }
            model.addAttribute("memberFormDto", memberFormDto);
        }catch (EntityNotFoundException e){
            System.out.println("Exception!!!!!!!!!");
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            model.addAttribute("MemberInfoCheckFormDto", memberInfoCheckFormDto);
            return "member/memberInfoCheckForm";
        }
        return "member/memberForm";
    }

    @PostMapping(value = "/modify")
    public String memberModify(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        Member.validationCheck(memberFormDto, bindingResult);
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        try {
            memberService.updateMember(memberFormDto, passwordEncoder);
        } catch (Exception e){
            model.addAttribute("errorMessage", "회원 수정 중 에러가 발생하였습니다.");
            return "member/memberForm";
        }
        return "redirect:/";
    }
}
