package in.tejas.rana.demospringwithsecuritycontext.component;

import in.tejas.rana.demospringwithsecuritycontext.dao.AuthToken;
import in.tejas.rana.demospringwithsecuritycontext.service.AccessTokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final AccessTokenService accessTokenService;

    public JwtAuthenticationProvider(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getPrincipal();
        AuthToken authToken = accessTokenService.parseAccessToken(token);
        return new UsernamePasswordAuthenticationToken(authToken.getUserId(), authToken.getSerialized(), authToken.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isInterface();
    }
}
