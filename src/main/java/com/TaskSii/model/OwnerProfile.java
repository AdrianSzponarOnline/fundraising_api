package com.TaskSii.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "owner_profiles")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class OwnerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_ownerprofile_user"))
    private User user;

    @Column(length = 100, nullable = false)
    private String organizationName;

    @Column(unique = true, nullable = false, length = 10)
    private String nip;

    @Column(unique = true, length = 14)
    private String regon;

    @Column(unique = true, length = 10)
    private String krs;

    @Column(unique = true, length = 15)
    private String phoneNumber;

    @Column(unique = true, length = 255, nullable = false)
    private String email;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "ownerProfile",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<FundraisingEvent> fundraisingEvents = new ArrayList<>();

    @OneToMany(mappedBy = "ownerProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Volunteer> volunteers = new ArrayList<>();



    public void addAddress(Address address) {
        addresses.add(address);
        address.setOwner(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setOwner(null);
    }

    public void addFundraisingEvent(FundraisingEvent fundraisingEvent) {
        fundraisingEvents.add(fundraisingEvent);
        fundraisingEvent.setOwnerProfile(this);
    }
    public void removeFundraisingEvent(FundraisingEvent fundraisingEvent) {
        fundraisingEvents.remove(fundraisingEvent);
        fundraisingEvent.setOwnerProfile(null);
    }
    public void addVolunteer(Volunteer volunteer) {
        volunteers.add(volunteer);
        volunteer.setOwnerProfile(this);
    }

    public void removeVolunteer(Volunteer volunteer) {
        volunteers.remove(volunteer);
        volunteer.setOwnerProfile(null);
    }
}
