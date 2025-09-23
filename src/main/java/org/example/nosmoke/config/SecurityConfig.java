package org.example.nosmoke.config;

import lombok.RequiredArgsConstructor;
import org.example.nosmoke.util.JwtAuthenticationFilter;
import org.example.nosmoke.util.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 설정파일 작성 어노테이션
@EnableWebSecurity // Spring Security를 활성화하는 어노테이션
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() { // 비밀번호 암호화 함수
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 보호 비활성화(Stateless API 에서는 불필요)
                .csrf(csrf -> csrf.disable())

                // 2. 세션 관리 정책 설정 --> Stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. HTTP Basic 및 Form Login 인증 방식 비활성화
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())

                // 4. 요청별 인가 규칙 설정 --> requestMatchers 경로 외 다른 경로(프로필 조회 등등)는 인증을 거쳐야만 접근할 수 있다고 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/auth/check-email",
                                // Swagger UI
                                "/swagger-resources/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated() // 위 3개 주소 외 인증(유효한 JWT를 가진 상태, 즉 로그인 상태)을 거쳐야만 접근 허용하겠다는 규칙 적용
                )

                // Security의 기본 필터(UsernamePasswordAuthenticationFilter) 실행 전에 JwtAuthenticationFilter를 앞에 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
