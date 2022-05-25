package by.bsuir.tattooparlor.entity;


import by.bsuir.tattooparlor.entity.helpers.OrderStatus;
import by.bsuir.tattooparlor.util.DateUtils;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity(name = "scheduled_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ScheduledItem {
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
    private String clientName;
    @NonNull
    private String contactPhone;

    @Transient
    private boolean isRealOrder;

    @Transient
    private String imageUri;

    @Transient
    private String tariffDescription;

    @ManyToOne
    @JoinColumn(name = "tattoo_master_id")
    private TattooMaster master;

    public ScheduledItem(Order order) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus();
        this.dateTime = order.getDateTime();
        this.comment = order.getComment();
        this.price = order.getPrice();
        this.clientName = order.getClient().getName();
        this.contactPhone = order.getContactPhone();
        this.master = order.getMaster();
        this.imageUri = order.getImageUri();
        this.tariffDescription = order.getTariff();
        this.isRealOrder = true;
    }

    public String getPriceFormatted() {
        return price == 0 ? "Не указано" : (price + " BYN");
    }

    public String getImageUri() {
        return imageUri == null ? "" : imageUri;
    }

    public String getDateFormatted() {
        return DateUtils.dateTimeToHtmlString(dateTime);
    }

    public String getDateFormattedISO() {
        return DateUtils.dateTimeToHtmlStringISO(dateTime);
    }

    public String getShortDateFormatted() {
        return DateUtils.dateToHtmlString(dateTime);
    }

    public String getShortDateFormattedISO() {
        return DateUtils.dateToHtmlStringISO(dateTime);
    }

    public String getTariff() {
        return tariffDescription == null ? "Не указано" : tariffDescription;
    }

    public String getStatusFormatted() {
        return orderStatus.getDescription();
    }

    public String getMinDateTime() {
        return DateUtils.dateTimeToHtmlStringISO(DateUtils.toDate(LocalDate.now().plusDays(1)));
    }

    public String getMaxDateTime() {
        return DateUtils.dateTimeToHtmlStringISO(DateUtils.toDate(LocalDate.now().plusYears(2)));
    }

    public int getDiscountedPrice() {
        return price;
    }

    public boolean isRealOrder() {
        return isRealOrder;
    }
}
