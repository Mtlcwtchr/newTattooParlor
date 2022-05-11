package by.bsuir.tattooparlor.entity;


import lombok.*;

import javax.persistence.*;

@Entity(name = "client_rate")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class ClientRate {
    @Id
    private long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NonNull
    private int mark;
}
