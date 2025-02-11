package training.ex.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class User {
    @Id
    private String userUuid;
    private String userEmail;
    private String userPassword;
    private String username;
    private String userStatus;
    private String socialProvider;
    private String role;
}
