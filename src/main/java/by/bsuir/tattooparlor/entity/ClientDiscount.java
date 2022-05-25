package by.bsuir.tattooparlor.entity;

import by.bsuir.tattooparlor.entity.helpers.ClientDiscountStatus;
import by.bsuir.tattooparlor.entity.helpers.DiscountStatus;
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

    public String getStatusDescription() {
        DiscountStatus discountStatus = discount.getStatus();
        ClientDiscountStatus clientDiscountStatus = getStatus();

        if (discountStatus == DiscountStatus.EXPIRED) {
            return "Истекла";
        }

        return switch (clientDiscountStatus) {
            case ACTIVE -> "Активна";
            case APPLYING -> "Применена к заказу";
            case APPLIED -> "Использована";
        };
    }
}
