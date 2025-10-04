package com.TaskSii.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fundraising_event")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class FundraisingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 255)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal accountBalance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_fundraising_owner"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OwnerProfile ownerProfile;

    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "fundraisingEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CollectionBox> collectionBoxes = new ArrayList<>();

    public void addCollectionBox(CollectionBox box) {
        collectionBoxes.add(box);
        box.setFundraisingEvent(this);
    }

    public void removeCollectionBox(CollectionBox box) {
        collectionBoxes.remove(box);
        box.setFundraisingEvent(null);
    }
}
