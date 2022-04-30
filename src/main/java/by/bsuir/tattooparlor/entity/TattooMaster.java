package by.bsuir.tattooparlor.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "tattoo_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TattooMaster extends User {
    @NonNull
    private String name;
    @NonNull
    private Date workStarted;
    private int rating;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;
}
