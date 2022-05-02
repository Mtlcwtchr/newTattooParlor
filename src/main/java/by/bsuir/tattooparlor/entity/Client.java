package by.bsuir.tattooparlor.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Client {
    @Id
    private long id;
    @NonNull
    private String name;
    @NonNull
    private String phone;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ClientDiscount> discounts;
}
