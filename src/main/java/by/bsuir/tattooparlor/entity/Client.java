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
    private String profilePictureUri;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ClientDiscount> discounts;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ClientRate> rates;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Order> orders;
}
