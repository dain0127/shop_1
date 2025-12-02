package com.changin.shop.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //login 관련 설정
        http.formLogin(form -> form
                        .loginPage("/member/login") //로그인 페이지 url
                        .defaultSuccessUrl("/") //로그인 성공시 이동 페이지
                        .failureUrl("/member/login/error") //로그인 실패시
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .permitAll() //?
                );

        http.logout(logout ->
                logout.logoutUrl("/member/logout")
                        .logoutSuccessUrl("/member/login"));


        http.authorizeHttpRequests(request -> request
                //해당 css또한 권한 무관하게 허용해야, style이 제대로 적용됨.
                .requestMatchers("/css/**", "/js/**").permitAll()
                //request에서 해당 url의 인증 절차 없이 허용
                .requestMatchers("/", "/member/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated() //다른 request에 대해서는 인증 요청
            );


        //인증되지 않은 페이지로, 사용자가 들어갔을 때의, '예의 처리'
        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

