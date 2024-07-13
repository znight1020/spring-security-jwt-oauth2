package training.ex.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import training.ex.auth.dto.CustomOAuth2User;
import training.ex.auth.model.User;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String authorization = null;

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }

        if(authorization == null) {
            log.info("토큰이 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization;

        if(isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = new User();
        user.setUsername(username);
        user.setRole(role);

        // UserDetails 에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(user, Map.of());

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private boolean isExpired(String token){
        if(jwtUtil.isExpired(token)) {
            log.info("만료된 토큰입니다.");
            return true;
        }
        return false;
    }
}
