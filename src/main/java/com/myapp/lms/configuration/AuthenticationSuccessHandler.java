package com.myapp.lms.configuration;

import com.myapp.lms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Configuration
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final MemberService memberService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        memberService.loginHistory(request);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
