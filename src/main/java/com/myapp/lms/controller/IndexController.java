package com.myapp.lms.controller;

import com.myapp.lms.component.MailComponents;
import com.myapp.lms.member.entity.Member;
import com.myapp.lms.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


@RequiredArgsConstructor
@Controller
public class IndexController {
    private final MailComponents mailComponents;
    private final MemberRepository memberRepository;

    @RequestMapping("/")
    public String index(){
//        String email = "teran1024@naver.com";
//        String subject = "안녕하세요";
//        String text = "<p>안녕하세요.</p><p>반갑습니다</p>";

//        mailComponents.sendMail(email, subject, text);
//        mailComponents.sendMailTest();
        return "index";
    }

    @RequestMapping("/error/denied")
    public String errorDenied(){
        return "error/denied";
    }

}
