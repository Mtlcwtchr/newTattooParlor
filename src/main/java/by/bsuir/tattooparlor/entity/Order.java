package by.bsuir.tattooparlor.entity;

import by.bsuir.tattooparlor.entity.helpers.OrderStatus;
import by.bsuir.tattooparlor.util.DateUtils;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        return price + " BYN";
    }

    public String getDateFormatted() {
        return DateUtils.dateTimeToHtmlString(dateTime);
    }

    public String getShortDateFormatted() {
        return DateUtils.dateToHtmlString(dateTime);
    }

    public String getTariff() {
        return product.getDifficultyDescription();
    }

    public String getStatusFormatted() {
        return orderStatus.getDescription();
    }

    public Date getMinDateTime() {
        return DateUtils.toDate(LocalDate.now().plusDays(1));
    }

    public Date getMaxDateTime() {
        return DateUtils.toDate(LocalDate.now().plusYears(2));
    }
}
