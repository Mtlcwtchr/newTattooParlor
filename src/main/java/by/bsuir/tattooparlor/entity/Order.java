package by.bsuir.tattooparlor.entity;

import by.bsuir.tattooparlor.entity.helpers.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "t_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Order {
    @Id
    private long id;
    @NonNull
    private OrderStatus orderStatus = OrderStatus.REQUESTED;
    @NonNull
    private Date dateTime;
    @NonNull
    private String comment;
    @NonNull
    private int price;
    @NonNull
    private String contactPhone;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private ClientDiscount discount;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "tattoo_master_id")
    private TattooMaster master;

    public String getPriceFormatted() {
        return price + "р";
    }
}
