package com.myapp.lms.configuration;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String msg = "로그인에 실패했습니다.";
        if(exception instanceof InternalAuthenticationServiceException){
            msg = exception.getMessage();
        }else{
            msg = exception.getMessage();
            if(exception.equals("자격 증명에 실패하였습니다.")){
                msg = "아이디나 비밀번호가 틀립니다.";
            }
        }
        setUseForward(true);
        setDefaultFailureUrl("/member/login?error=true");
        request.setAttribute("errorMessage", msg);
        System.out.println(msg);
        super.onAuthenticationFailure(request, response, exception);
    }
}
