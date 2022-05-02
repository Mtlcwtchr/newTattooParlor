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
public class TattooMaster {
    @Id
    private long id;
    @NonNull
    private String name;
    @NonNull
    private Date workStarted;
    private int rating;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;
}
