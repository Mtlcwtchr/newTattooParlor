package by.bsuir.tattooparlor.entity;


import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.entity.helpers.UserStatus;
import lombok.*;
import org.hibernate.annotations.Generated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name = "t_user")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    @Id
    private long id;
    @NonNull
    private String login;
    @NonNull
    private String password;
    @NonNull
    private String email;
    @NonNull
    private UserRole role = UserRole.CLIENT;
    @NonNull
    private UserStatus status = UserStatus.UNCONFIRMED;
}
