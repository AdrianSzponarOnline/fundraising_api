package com.TaskSii.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "collection_box")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class CollectionBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private boolean empty = true;

    @Column(nullable = false)
    private LocalDateTime created;

    private LocalDateTime collectedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_collectionbox_event"
                ))
    private FundraisingEvent fundraisingEvent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "fk_collectionbox_volunteer"))
    private Volunteer volunteer;

    @OneToMany(mappedBy = "collectionBox", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    private List<BoxMoney> transfers = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (created == null) {
            created = LocalDateTime.now();
        }
    }

    public BigDecimal getTotalAmount() {
        return transfers.stream()
                .filter(boxMoney -> !boxMoney.isTransferred())
                .map(BoxMoney::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addTransfer(BoxMoney transfer) {
        transfers.add(transfer);
        transfer.setCollectionBox(this);
        this.empty = false;
    }
    public void removeTransfer(BoxMoney transfer) {
        transfers.remove(transfer);
        transfer.setCollectionBox(null);
        if(transfers.isEmpty()) this.empty = true;
    }

    public FundraisingEvent getFundraisingEvent() {
        return fundraisingEvent;
    }

    public void setFundraisingEvent(FundraisingEvent fundraisingEvent) {
        this.fundraisingEvent = fundraisingEvent;
    }
}
