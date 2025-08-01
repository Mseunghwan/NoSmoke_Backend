package org.example.nosmoke.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 1. 헤더에서 JWT 를 받아옵니다
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        // 2. 유효한 토큰인지 확인합니다
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // "Bearer " 접두사 제거
            if (token.startsWith("Bearer")) {
                token = token.substring(7);
            }

            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장합니다
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
