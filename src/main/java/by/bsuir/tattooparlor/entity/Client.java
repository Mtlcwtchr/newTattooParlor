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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Client extends User {
    @NonNull
    private String name;
    @NonNull
    private String phone;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ClientDiscount> discounts;
}
