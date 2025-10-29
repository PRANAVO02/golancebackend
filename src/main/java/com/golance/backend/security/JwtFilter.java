package com.golance.backend.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.golance.backend.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // TODO Auto-generated method stub

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer")) {  //if it does not have a bearer token, then it is simply passed to the next filter (refer SecurityConfig)
            filterChain.doFilter(request, response);
            return;

        }

        String token = authHeader.substring(7); //token is like BEARER xxxxxx  ->starts from the 7th index

        try {
            String username = jwtUtil.extractUsername(token);

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {

                UserDetails userDetails = userService.loadUserByUsername(username);

                //now that we have both user details and token. now we have to validate token and user details

                if(jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }catch (Exception e) {

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("error", "Invalid token");

            ObjectMapper objMapper = new ObjectMapper();
            String jsonString = objMapper.writeValueAsString(responseMap);

            response.getWriter().write(jsonString);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;

        }

        filterChain.doFilter(request, response);
    }



}