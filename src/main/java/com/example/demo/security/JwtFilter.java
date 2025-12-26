package com.example.demo.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest r = (HttpServletRequest) req;
        String auth = r.getHeader("Authorization");

        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                jwtUtil.getAllClaims(auth.substring(7));
            } catch (Exception ignored) {}
        }
        chain.doFilter(req, res);
    }
}
