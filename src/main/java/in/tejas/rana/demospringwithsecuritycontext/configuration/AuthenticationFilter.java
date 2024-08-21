package in.tejas.rana.demospringwithsecuritycontext.configuration;

import in.tejas.rana.demospringwithsecuritycontext.dao.AuthToken;
import in.tejas.rana.demospringwithsecuritycontext.dao.UserDao;
import in.tejas.rana.demospringwithsecuritycontext.dto.UserDto;
import in.tejas.rana.demospringwithsecuritycontext.service.AccessTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AccessTokenService accessTokenService;
    private final UserDao userDao;

    public AuthenticationFilter(AccessTokenService accessTokenService, UserDao userDao) {
        this.accessTokenService = accessTokenService;
        this.userDao = userDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.toLowerCase().startsWith("bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            AuthToken authToken = accessTokenService.parseAccessToken(authHeader.substring(7));
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null && authToken.getUserId() != null) {
                UserDto userDto = userDao.getUserById(authToken.getUserId());
                List<SimpleGrantedAuthority> authorities = userDto.getRoles().stream().map(role -> {
                    String newRole = "ROLE_" + role.getTitle().trim().toUpperCase().replaceAll(" ", "_");
                    return new SimpleGrantedAuthority(newRole);
                }).toList();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authToken.getUserId(), authToken.getSerialized(), authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception exception) {
            logger.error(exception);
            throw exception;
        }

        filterChain.doFilter(request, response);
    }
}
