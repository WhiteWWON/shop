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
import org.springframework.validation.FieldError;
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
    public String newMember(@Valid MemberFormDto memberFormDto,
                            BindingResult bindingResult, Model model){
        /*  비밀번호 입력 확인  --> 프론트에서 스크립트 체크로 */
        /* Member class 에서 하도록 수정 또는 프론트 */
        if(!memberFormDto.getPassword().equals(memberFormDto.getPassword_chk())){
            FieldError fieldError = new FieldError("memberFormDto", "password_chk", "비밀번호가 일치하지 않습니다.");
            bindingResult.addError(fieldError);
        }
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
            /* 비밀번호 확인 */
            /* 메서드로 처리하도록 변경 */
            if(!passwordEncoder.matches(memberInfoCheckFormDto.getPassword(), memberFormDto.getPassword())) {
                FieldError fieldError = new FieldError("memberInfoCheckForm", "password", "입력하신 비밀번호가 일치하지 않습니다.");
                bindingResult.addError(fieldError);
                return "member/memberInfoCheckForm";
            }
            model.addAttribute("memberFormDto", memberFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            model.addAttribute("MemberFormDto", new MemberFormDto());
            return "member/memberForm";
        }
        return "member/memberForm";
    }

    @PostMapping(value = "/modify")
    public String memberModify(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        /*  비밀번호 입력 확인 */
        if(!memberFormDto.getPassword().equals(memberFormDto.getPassword_chk())){
            FieldError fieldError = new FieldError("memberFormDto", "password_chk", "비밀번호가 일치하지 않습니다.");
            bindingResult.addError(fieldError);
        }
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        try {
            memberService.updateMember(memberFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", "회원 수정 중 에러가 발생하였습니다.");
            return "member/memberForm";
        }

        return "redirect:/";
    }
}
