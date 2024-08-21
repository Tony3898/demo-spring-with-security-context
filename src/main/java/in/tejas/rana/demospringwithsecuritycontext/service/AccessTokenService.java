package in.tejas.rana.demospringwithsecuritycontext.service;

import in.tejas.rana.demospringwithsecuritycontext.dao.AuthToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService extends TokenService {

    static Log logger = LogFactory.getLog(AccessTokenService.class);

    public AccessTokenService(JWTService jwtService) {
        super(jwtService);
    }

    @Override
    protected void addTokenToHeader(HttpHeaders responseHeaders, AuthToken authToken) {
        responseHeaders.set("x-auth-key", authToken.getSerialized());
        responseHeaders.setExpires(authToken.getExpiryTime());
    }

    public AuthToken parseAccessToken(String token) {
        return this.parseToken(token);
    }
}
