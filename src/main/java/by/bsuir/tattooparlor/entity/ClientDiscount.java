package by.bsuir.tattooparlor.entity;

import by.bsuir.tattooparlor.entity.helpers.ClientDiscountStatus;
import lombok.*;

import javax.persistence.*;

@Entity(name = "client_discount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ClientDiscount {
    @Id
    private long id;
    @NonNull
    private ClientDiscountStatus status = ClientDiscountStatus.ACTIVE;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client owner;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
}
