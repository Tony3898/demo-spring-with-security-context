package in.tejas.rana.demospringwithsecuritycontext.repository;

import in.tejas.rana.demospringwithsecuritycontext.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findById(long id);
}