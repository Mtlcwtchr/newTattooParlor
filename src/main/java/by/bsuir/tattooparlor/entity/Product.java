package by.bsuir.tattooparlor.entity;

import by.bsuir.tattooparlor.entity.helpers.GalleryType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private int colorsCount;
    @NonNull
    private String imageUri;
    @NonNull
    private GalleryType galleryType = GalleryType.GALLERY;
    @NonNull
    private int difficulty;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ClientRate> rates;

    public long getLikesCount() {
        if(rates == null) {
            return 0;
        }

        return rates.stream().filter(rate -> rate.getMark() > 0).count();
    }

    public long getDislikesCount() {
        if(rates == null) {
            return 0;
        }

        return rates.stream().filter(rate -> rate.getMark() < 0).count();
    }

    public String getDifficultyDescription() {
        return (difficulty > 60 ? "Сложная" : "Простая") +
               (colorsCount > 1 ? " Цветная" : " Черно-белая");
    }

    public boolean isLikedByClient(Client client) {
        if(client == null) {
            return false;
        }
        return rates.stream().anyMatch(clientRate -> clientRate.getClient().getId() == client.getId() && clientRate.getMark() > 0);
    }

    public boolean isDislikedByClient(Client client) {
        if(client == null) {
            return false;
        }
        return rates.stream().anyMatch(clientRate -> clientRate.getClient().getId() == client.getId() && clientRate.getMark() < 0);
    }
}
