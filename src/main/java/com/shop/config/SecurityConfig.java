package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new CookieCsrfTokenRepository())   // csrf 토큰 저장소를 http only cookie로 설정
                )
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated()
                ).formLogin(formLoginCustomizer -> formLoginCustomizer
                        .loginPage("/members/login")
                        .defaultSuccessUrl("/", true)
                        .usernameParameter("email")
                        .failureHandler(new FormLoginAuthenticationFailureHandler())
                ).logout( logoutCustomizer -> logoutCustomizer
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/")
                ).exceptionHandling(e -> e
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .build()
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
