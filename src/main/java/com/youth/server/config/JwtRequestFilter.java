//    package com.youth.server.config;
//
//    import com.youth.server.domain.User;
//    import com.youth.server.service.AuthService;
//    import jakarta.servlet.FilterChain;
//    import jakarta.servlet.ServletException;
//    import jakarta.servlet.http.Cookie;
//    import jakarta.servlet.http.HttpServletRequest;
//    import jakarta.servlet.http.HttpServletResponse;
//    import lombok.RequiredArgsConstructor;
//    import org.springframework.context.annotation.Lazy;
//    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//    import org.springframework.security.core.GrantedAuthority;
//    import org.springframework.security.core.authority.SimpleGrantedAuthority;
//    import org.springframework.security.core.context.SecurityContextHolder;
//    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//    import org.springframework.stereotype.Component;
//    import org.springframework.web.filter.OncePerRequestFilter;
//
//    import java.io.IOException;
//    import java.util.Arrays;
//    import java.util.Collection;
//    import java.util.List;
//    import java.util.Optional;
//
//    @Component
//    @RequiredArgsConstructor
//    public class JwtRequestFilter extends OncePerRequestFilter {
//        @Lazy
//        private final AuthService authService;
//
//        @Override
//        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//            Cookie[] cookies = request.getCookies();
//            String jwt = null;
//
//            System.out.println("쿠키 전체: \n" + Arrays.toString(cookies));
//            // 쿠키 전체 중 쿠키 가져오기
//            if (cookies == null) {
//                System.out.println("인증 실패: 쿠키가 없는 요청");
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            // 쿠키 전체 중 토큰 가져오기
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("token")) {
//                    System.out.println("토큰: " + cookie.getValue());
//                    jwt = cookie.getValue();
//                }
//            }
//
//            // 토큰이 없으면 다음 필터로 넘어감
//            if (jwt == null) {
//                System.out.println("인증 실패: jwt is null");
//                filterChain.doFilter(request, response);
//                return;
//            }
//            Optional<User> currentUser;
//
//            currentUser = authService.getUserFromToken(jwt);// 쿠키 검증 실패 (토큰이 잘못됨)
//
//
//            if(currentUser.isEmpty()) {
//                System.out.println("인증 실패: jwt is invalid");
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//
//            // 인정과 관련된 객체 생성
//            Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_"+currentUser.get().getIsAdmin().toString()));
//
//            // 인증 객체 생성
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(currentUser, null, authorities);
//
//            System.out.println("권한 생성됨"+ currentUser.get().getIsAdmin().toString());
//
//            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            // 인증 객체를 시큐리티 컨텍스트에 저장
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//
//            filterChain.doFilter(request, response);
//        }
//
//
//    }
