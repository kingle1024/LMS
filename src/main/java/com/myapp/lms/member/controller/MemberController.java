package com.myapp.lms.member.controller;

import com.myapp.lms.member.model.MemberInput;
import com.myapp.lms.member.model.ResetPasswordInput;
import com.myapp.lms.member.repository.MemberRepository;
import com.myapp.lms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor // memberService의 생성자가 추가되지 않아도 됨
@Controller
public class MemberController {

    private final MemberService memberService;

    @RequestMapping("/member/login")
    public String login(){

        return "member/login";
    }
    @GetMapping("/member/find/password")
    public String findPassword(){
        return "member/find_password";
    }
    @PostMapping("/member/find/password")
    public String findPasswordSubmit(
            ResetPasswordInput resetPasswordInput,
            Model model
    ){
        System.out.println(resetPasswordInput.toString());

        boolean result = false;
        try {
            result = memberService.sendResetPassword(resetPasswordInput);
        }catch(Exception e){

        }
        model.addAttribute("result", result);

        return "member/find_password_result";
    }
    //member/register
    @GetMapping("/member/register")
    public String register(){
        return "member/register";
    }

    // request WEB -> SERVER
    // response SERVER -> WEB
    @PostMapping("/member/register")
    public String registerSubmit(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            MemberInput parameter
    ){
        boolean result = memberService.register(parameter);
        model.addAttribute("result", result);
        return "member/register_complete";
    }
    @GetMapping("/member/reset/password")
    public String resetPassword(
            Model model,
            HttpServletRequest request){
        String uuid = request.getParameter("id");
        model.addAttribute("uuid", uuid);

        boolean result = memberService.checkResetPassword(uuid);
        model.addAttribute("result", result);

        return "member/reset_password";
    }
    @PostMapping("/member/reset/password")
    public String resetPasswordSubmit(
            Model model,
            ResetPasswordInput parameter){

        boolean result = false;
        try {
            result = memberService.resetPassword(
                    parameter.getId(), parameter.getPassword());
        }catch(Exception e){
            System.out.println("ERROR : "+e);
        }
        model.addAttribute("parameter", parameter);
        model.addAttribute("result", result);

//        return "member/reset_password";
        return "member/reset_password_result";
    }

    @GetMapping("/email-auth")
    public String emailAuth(Model model, HttpServletRequest request){
        String uuid = request.getParameter("id");
        boolean result = memberService.emailAuth(uuid);
        model.addAttribute("result", result);
        return "member/email_auth";
    }

    @GetMapping("/member/info")
    public String memberInfo(){
        return "member/info";
    }

}
