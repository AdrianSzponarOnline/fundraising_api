package com.TaskSii.model;

import jakarta.persistence.*;
import org.springframework.context.annotation.Profile;

@Entity
@Table(name = "addresses")
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

    public Address() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public OwnerProfile getOwner() {
        return owner;
    }

    public void setOwner(OwnerProfile owner) {
        this.owner = owner;
    }
}
