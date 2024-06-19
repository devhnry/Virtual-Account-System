package org.henry.virtualaccountsystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.henry.virtualaccountsystem.repository.TokenRepository;
import org.henry.virtualaccountsystem.service.JWTService;
import org.henry.virtualaccountsystem.service.UserDetailService;
import org.henry.virtualaccountsystem.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.function.Function;

@Component
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailService userDetailService;
    private final TokenRepository tokenRepository;

    @Autowired
    public JwtSecurityFilter(JWTService jwtService, UserDetailService userDetailService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.userDetailService = userDetailService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException{
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
        if(authHeader == null || authHeader.isBlank()){
            filterChain.doFilter(request,response);
            return;
        }
        jwtToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwtToken);

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailService.loadUserByUsername(userEmail);

            Function<Token, Boolean> validateToken = t -> !t.getExpired().equals(true) && !t.getRevoked().equals(true);

            var isTokenValid = tokenRepository.findByToken(jwtToken).map(
                    validateToken
            ).orElse(false);


            if(jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }
}
