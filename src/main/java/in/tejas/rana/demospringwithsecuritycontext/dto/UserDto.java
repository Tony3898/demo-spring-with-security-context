package in.tejas.rana.demospringwithsecuritycontext.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.tejas.rana.demospringwithsecuritycontext.model.Role;
import in.tejas.rana.demospringwithsecuritycontext.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    Long id;
    String email;
    String username;
    String name;
    Boolean enabled;
    Set<Role> roles = new HashSet<>();

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.name = user.getName();
        this.enabled = user.getEnabled();
        this.roles = user.getRoles();
    }

    public UserDto getUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getUsername(), user.getName(), user.getEnabled(), user.getRoles());
    }

    @JsonIgnore
    public User getUser() {
        User user = new User();
        user.setId(this.getId());
        user.setName(this.getName());
        user.setUsername(this.getUsername());
        user.setEmail(this.getEmail());
        user.setEnabled(this.getEnabled());
        user.setRoles(this.getRoles());
        return user;
    }
}