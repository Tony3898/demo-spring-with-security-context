package in.tejas.rana.demospringwithsecuritycontext.service;

import in.tejas.rana.demospringwithsecuritycontext.dao.AuthToken;
import in.tejas.rana.demospringwithsecuritycontext.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public abstract class TokenService {

    private final JWTService jwtService;

    private static final String USER_TYPE_CLAIM = "utp";
    private static final String TOKEN_TYPE_CLAIM = "ttp";


    protected static Log logger = LogFactory.getLog(TokenService.class);

    public TokenService(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    protected abstract void addTokenToHeader(HttpHeaders responseHeaders, AuthToken authToken);

    public void addTokenToHeader(HttpHeaders headers, UserDto user, String audience) {
        this.addTokenToHeader(headers, user, audience, 7 * 24 * 3600);
    }

    public void addTokenToHeader(HttpHeaders headers, UserDto user, String audience, Integer expiry) {
        AuthToken authToken = createToken(String.valueOf(user.getId()), audience, expiry);
        addTokenToHeader(headers, authToken);
    }

    private AuthToken createToken(String subject, String audience, Integer expiry) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE_CLAIM, "auth");
        claims.put(USER_TYPE_CLAIM, "U");
        String token = this.jwtService.encodeClaims(claims, subject, expiry, audience);
        return this.parseToken(token);
    }

    protected AuthToken parseToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new JwtException("Invalid Auth Token");
        }
        Claims claims = this.jwtService.decodeClaims(token);
        AuthToken authToken = new AuthToken();
        authToken.setAudience(String.join(",", claims.getAudience()));
        authToken.setUserId(Long.valueOf(claims.getSubject()));
        authToken.setIssueTime(claims.getIssuedAt().getTime());
        authToken.setExpiryTime(claims.getExpiration().getTime());
        authToken.setSerialized(token);
        return authToken;
    }
}
