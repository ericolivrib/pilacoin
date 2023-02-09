package br.ufsm.poli.csi.tapw.pilacoin.security.filter;

import br.ufsm.poli.csi.tapw.pilacoin.security.util.CookieUtil;
import br.ufsm.poli.csi.tapw.pilacoin.security.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_COOKIE = "token";
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String url = request.getRequestURI();

        try {
            var token = CookieUtil.getCookie(TOKEN_COOKIE, request.getCookies()).orElse(null);
            var username = JwtUtil.getSubjectToken(token);
            var authentication = SecurityContextHolder.getContext().getAuthentication();

            if (!url.contains("login")) {
                if (username != null && authentication == null) {
                    var userDetails = this.userDetailsService.loadUserByUsername(username);
                    boolean tokenExpirado = JwtUtil.isExpiredToken(token);

                    if (!tokenExpirado) {
                        var wads = new WebAuthenticationDetailsSource();
                        var details = wads.buildDetails(request);
                        var authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                Collections.emptyList()
                        );

                        authToken.setDetails(details);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            CookieUtil.removeCookie(TOKEN_COOKIE, request);
            response.sendRedirect("/login");
        } catch (MalformedJwtException e) {
            CookieUtil.removeCookie(TOKEN_COOKIE, request);
            // response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token inválido");
            response.sendRedirect("/login");
            // response.sendRedirect();
        }
    }

}
