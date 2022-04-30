package by.bsuir.tattooparlor.entity;

import by.bsuir.tattooparlor.entity.helpers.GalleryType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

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
    private long id;
    @NonNull
    private int colorsCount;
    @NonNull
    private String imageUri;
    @NonNull
    private GalleryType galleryType = GalleryType.GALLERY;
    @NonNull
    private int difficulty;
}
