package in.tejas.rana.demospringwithsecuritycontext.controller;

import in.tejas.rana.demospringwithsecuritycontext.service.AccessTokenService;
import in.tejas.rana.demospringwithsecuritycontext.service.UserService;
import org.springframework.stereotype.Controller;

@Controller
public class BaseRestController {
    protected final UserService userService;
    protected final AccessTokenService accessTokenService;

    public BaseRestController(UserService userService, AccessTokenService accessTokenService) {
        this.userService = userService;
        this.accessTokenService = accessTokenService;
    }
}
