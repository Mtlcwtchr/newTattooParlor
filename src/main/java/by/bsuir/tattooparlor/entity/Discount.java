package by.bsuir.tattooparlor.entity;

import by.bsuir.tattooparlor.entity.helpers.DiscountStatus;
import by.bsuir.tattooparlor.util.DateUtils;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
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
    private Date expireDate;
    @NonNull
    private String promocode;
    @NonNull
    private DiscountStatus status = DiscountStatus.ACTIVE;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL)
    private List<ClientDiscount> clientDiscounts;

    public String getExpireDateFormatted() {
        return DateUtils.dateToHtmlString(expireDate);
    }

    public String getValueFormatted() {
        return percentage + "%";
    }

    public String getStatusDescription() {
        return getStatus().getDescription();
    }

    public DiscountStatus getStatus() {
        return expireDate.after(new Date()) ? DiscountStatus.ACTIVE : DiscountStatus.EXPIRED;
    }
}
