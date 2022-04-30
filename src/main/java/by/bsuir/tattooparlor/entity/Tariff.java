package by.bsuir.tattooparlor.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "tariff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Tariff {
    @Id
    private long id;
    @NonNull
    private int normalizedValue;
}
