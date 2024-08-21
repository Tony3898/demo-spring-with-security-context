package in.tejas.rana.demospringwithsecuritycontext.controller;

import in.tejas.rana.demospringwithsecuritycontext.dto.UserDto;
import in.tejas.rana.demospringwithsecuritycontext.model.User;
import in.tejas.rana.demospringwithsecuritycontext.service.AccessTokenService;
import in.tejas.rana.demospringwithsecuritycontext.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableMethodSecurity
@RequestMapping("/api/user")
public class UserRestController extends BaseRestController {


    static Log logger = LogFactory.getLog(UserRestController.class);

    public UserRestController(UserService userService, AccessTokenService accessTokenService) {
        super(userService, accessTokenService);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, null, 200);
    }

    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @PostMapping("")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        try {
            User user = userService.createUser(userDto);
            return ResponseEntity.ok(new UserDto(user));
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

}
