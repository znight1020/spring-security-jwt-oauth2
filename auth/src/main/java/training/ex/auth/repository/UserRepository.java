package training.ex.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.ex.auth.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
}
