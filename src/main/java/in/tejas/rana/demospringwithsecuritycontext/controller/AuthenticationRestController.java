package in.tejas.rana.demospringwithsecuritycontext.controller;

import in.tejas.rana.demospringwithsecuritycontext.dto.LoginDto;
import in.tejas.rana.demospringwithsecuritycontext.dto.UserDto;
import in.tejas.rana.demospringwithsecuritycontext.service.AccessTokenService;
import in.tejas.rana.demospringwithsecuritycontext.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/auth", "/api"})
public class AuthenticationRestController extends BaseRestController {

    public AuthenticationRestController(UserService userService, AccessTokenService accessTokenService) {
        super(userService, accessTokenService);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }
}
