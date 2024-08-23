package com.shop.config;

import com.shop.service.MemberService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated()
                ).formLogin(formLoginCustomizer -> formLoginCustomizer
                        .loginPage("/members/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("email")
                        .failureUrl("/members/login/error")
                        .failureHandler(((request,  response, exception) -> {
                            // 특별한 처리 실패했을 때, "/members/login/error"로 리다이렉트만 하는 동작만 필요하다면  
                            // failureHandler() 설정 필요없이, failureUrl() 설정만으로도 충분함. 
                            log.info("### failureHandler ###: {}", exception.getMessage());
                            response.sendRedirect("/members/login/error");
                         }))
                ).logout( logoutCustomizer -> logoutCustomizer
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/")

                ).exceptionHandling(exceptionHandlingCustomizer-> exceptionHandlingCustomizer.authenticationEntryPoint((request, response, authException) -> {
                    if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                        log.info("### 보호된 리소스 접근 - ajax ### ");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    } else {
                        // 일반 HTTP 접근 요청은 formLogin() 설정에서 처리되서 항상 로그인 페이지로 가기 때문에
                        // 이 분기 자체가 필요가 없을 것 같긴하다.
                        log.info("### 보호된 리소스 접근 - no ajax ### ");
                        response.sendRedirect("/members/login/error");
                    }})
                ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}