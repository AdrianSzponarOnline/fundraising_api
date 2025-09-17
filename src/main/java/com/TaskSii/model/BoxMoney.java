package com.TaskSii.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "box_money")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class BoxMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "box_id", nullable = false)
    private CollectionBox collectionBox;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private boolean transferred = false;

    @Column(nullable = false)
    private boolean empty = true;
}
