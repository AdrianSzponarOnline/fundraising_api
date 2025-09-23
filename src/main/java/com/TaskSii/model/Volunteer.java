package com.TaskSii.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "volunteers")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(unique = true, length = 15)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_profile_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_volunteer_owner"))
    private OwnerProfile ownerProfile;

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CollectionBox> collectionBoxes = new ArrayList<>();

}
