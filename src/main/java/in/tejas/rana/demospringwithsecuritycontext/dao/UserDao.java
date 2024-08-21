package in.tejas.rana.demospringwithsecuritycontext.dao;

import in.tejas.rana.demospringwithsecuritycontext.dto.UserDto;
import in.tejas.rana.demospringwithsecuritycontext.model.User;
import in.tejas.rana.demospringwithsecuritycontext.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

@Configuration
public class UserDao {

    private final UserRepository userRepository;

    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDto().getUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(user -> new UserDto().getUserDto(user)).toList();
    }

    public User createUser(UserDto userDto) {
        User user = userDto.getUser();
        user.setEnabled(true);
        user.setUsername(userDto.getEmail());
        return userRepository.save(user);
    }

    public User updateUser(UserDto userDto) {
        User user = userDto.getUser();
        return userRepository.save(user);
    }

    public User getByEmailOrUsername(String email, String username) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = userRepository.findByUsername(username).orElse(null);
        }
        return user;
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
