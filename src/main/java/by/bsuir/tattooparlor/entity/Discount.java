package by.bsuir.tattooparlor.entity;

import by.bsuir.tattooparlor.entity.helpers.DiscountStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "discount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Discount {
    @Id
    private long id;
    @NonNull
    private int percentage;
    @NonNull
    private String description;
    @NonNull
    private String promocode;
    @NonNull
    private DiscountStatus status = DiscountStatus.ACTIVE;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL)
    private List<ClientDiscount> clientDiscounts;
}
