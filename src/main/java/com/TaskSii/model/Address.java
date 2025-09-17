package com.TaskSii.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Profile;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String streetName;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_address_owner")
    )
    private OwnerProfile owner;
}
