package by.bsuir.tattooparlor.entity;

import by.bsuir.tattooparlor.util.DateUtils;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.Date;

@Entity(name = "tattoo_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class TattooMaster {
    @Id
    private long id;
    @NonNull
    private String name;
    @NonNull
    private Date workStarted;
    private int rating;
    private String profilePictureUri;
    private String masterDescription;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    public int getExperience() {
        LocalDate now = LocalDate.now();
        LocalDate workStarted = DateUtils.toLocal(getWorkStarted());
        return Period.between(workStarted, now).getYears();
    }

    public String getDateOfWorkStartFormatted() {
        return DateUtils.dateToHtmlString(workStarted);
    }

    public String getDateOfWorkStartFormattedISO() {
        return DateUtils.dateToHtmlStringISO(workStarted);
    }
}
