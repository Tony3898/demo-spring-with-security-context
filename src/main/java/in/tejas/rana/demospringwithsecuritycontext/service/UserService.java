package in.tejas.rana.demospringwithsecuritycontext.service;

import in.tejas.rana.demospringwithsecuritycontext.dao.UserDao;
import in.tejas.rana.demospringwithsecuritycontext.dto.LoginDto;
import in.tejas.rana.demospringwithsecuritycontext.dto.UserDto;
import in.tejas.rana.demospringwithsecuritycontext.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;
    private final AccessTokenService accessTokenService;
    private final HttpServletRequest httpRequest;

    public UserService(UserDao userDao, AccessTokenService accessTokenService, HttpServletRequest httpRequest) {
        this.userDao = userDao;
        this.accessTokenService = accessTokenService;
        this.httpRequest = httpRequest;
    }

    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User createUser(UserDto userDto) {
        return userDao.createUser(userDto);
    }

    public ResponseEntity<UserDto> login(LoginDto loginDto) {
        User user = userDao.getByEmailOrUsername(loginDto.getEmail(), loginDto.getEmail());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        UserDto userDto = new UserDto(user);
        accessTokenService.addTokenToHeader(headers, userDto, httpRequest.getRequestURI());
        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }
}
