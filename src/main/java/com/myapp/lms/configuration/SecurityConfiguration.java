package com.myapp.lms.configuration;

import com.myapp.lms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final MemberService memberService;
    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean // Bean 안만들면 오류
    UserAuthenticationFailureHandler getFailurHandler(){
        return new UserAuthenticationFailureHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();

        http.authorizeRequests()
                .antMatchers( // 로그인이 없어도 접근 가능한 위치를 정의
                        "/",
                        "/member/register",
                        "/member/email-auth",
                        "/member/find/password",
                        "/member/reset/password",
                        "/email-auth"
                )
                        .permitAll();
        http.authorizeRequests()
                        .antMatchers("/admin/**")
                        .hasAuthority("ROLE_ADMIN");

        http.formLogin()
                .loginPage("/member/login")
                .failureHandler(getFailurHandler())
                .permitAll();

        http.exceptionHandling()
                .accessDeniedPage("/error/denied");

        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(getPasswordEncoder());
        super.configure(auth);
    }


}
