package com.dosol.qwer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // URL 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/login", "/join", "/joinProc", "/css/**", "/js/**").permitAll() // 누구나 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN") // ADMIN 권한 필요
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER") // USER 또는 ADMIN 접근 가능
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )

                // 로그인 설정
                .formLogin(auth -> auth
                        .loginPage("/login") // 커스텀 로그인 페이지 설정
                        .loginProcessingUrl("/loginProc") // 로그인 처리 URL
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 항상 '/'로 이동
                        .failureUrl("/login?error") // 로그인 실패 시 이동 URL
                        .permitAll()
                )

                // 로그아웃 설정
                .logout(auth -> auth
                        .logoutUrl("/logout") // 로그아웃 처리 URL
                        .logoutSuccessUrl("/login?logout") // 로그아웃 성공 시 이동 URL
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .permitAll()
                )


                // CSRF 비활성화 (개발용, 운영에서는 활성화 권장)
                .csrf(auth -> auth.disable());

        return http.build();
    }
}
