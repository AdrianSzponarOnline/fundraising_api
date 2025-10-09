package com.TaskSii.mapper;

import com.TaskSii.dto.AddressDTO;
import com.TaskSii.model.Address;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressMapperTest {
    private final AddressMapper mapper = Mappers.getMapper(AddressMapper.class);

    @Test
    void shouldMapAddressToAddress() {
        Address address = Address.builder()
                .id(1L)
                .streetName("Test Street")
                .city("Lublin")
                .state("Lubelskie")
                .country("Polska")
                .postalCode("20-001")
                .build();

        AddressDTO dto = mapper.toAddressDTO(address);

        assertThat(dto.id()).isEqualTo(address.getId());
        assertThat(dto.streetName()).isEqualTo(address.getStreetName());
        assertThat(dto.city()).isEqualTo(address.getCity());
        assertThat(dto.state()).isEqualTo(address.getState());
        assertThat(dto.country()).isEqualTo(address.getCountry());
        assertThat(dto.postalCode()).isEqualTo(address.getPostalCode());
    }

    @Test
    void shouldMapAddressDTOToAddress() {
        AddressDTO dto = new AddressDTO(
                "Test Street",
                "Lublin",
                "Lubelskie",
                "Polska",
                "20-001"
        );

        Address address = mapper.fromAddressDTO(dto);

        assertThat(address.getId()).isNull();
        assertThat(address.getOwner()).isNull();
        assertThat(address.getStreetName()).isEqualTo(dto.streetName());
        assertThat(address.getCity()).isEqualTo(dto.city());
        assertThat(address.getState()).isEqualTo(dto.state());
        assertThat(address.getCountry()).isEqualTo(dto.country());
        assertThat(address.getPostalCode()).isEqualTo(dto.postalCode());
    }

    @Test
    void shouldMapAddressToAddressDTOWithNullValues() {
        Address address = Address.builder()
                .id(null)
                .streetName(null)
                .city(null)
                .state(null)
                .country(null)
                .postalCode(null)
                .build();

        AddressDTO dto = mapper.toAddressDTO(address);

        assertThat(dto.id()).isNull();
        assertThat(dto.streetName()).isNull();
        assertThat(dto.city()).isNull();
        assertThat(dto.state()).isNull();
        assertThat(dto.country()).isNull();
        assertThat(dto.postalCode()).isNull();
    }

    @Test
    void shouldMapAddressDTOToAddressWithNullValues() {
        AddressDTO dto = new AddressDTO(
                null,
                null,
                null,
                null,
                null
        );

        Address address = mapper.fromAddressDTO(dto);

        assertThat(address.getId()).isNull();
        assertThat(address.getOwner()).isNull();
        assertThat(address.getStreetName()).isNull();
        assertThat(address.getCity()).isNull();
        assertThat(address.getState()).isNull();
        assertThat(address.getCountry()).isNull();
        assertThat(address.getPostalCode()).isNull();
    }

    @Test
    void shouldMapAddressToAddressDTOWithEmptyStrings() {
        Address address = Address.builder()
                .id(1L)
                .streetName("")
                .city("")
                .state("")
                .country("")
                .postalCode("")
                .build();

        AddressDTO dto = mapper.toAddressDTO(address);

        assertThat(dto.id()).isEqualTo(address.getId());
        assertThat(dto.streetName()).isEqualTo("");
        assertThat(dto.city()).isEqualTo("");
        assertThat(dto.state()).isEqualTo("");
        assertThat(dto.country()).isEqualTo("");
        assertThat(dto.postalCode()).isEqualTo("");
    }

    @Test
    void shouldMapAddressDTOToAddressWithEmptyStrings() {
        AddressDTO dto = new AddressDTO(
                "",
                "",
                "",
                "",
                ""
        );

        Address address = mapper.fromAddressDTO(dto);

        assertThat(address.getId()).isNull();
        assertThat(address.getOwner()).isNull();
        assertThat(address.getStreetName()).isEqualTo("");
        assertThat(address.getCity()).isEqualTo("");
        assertThat(address.getState()).isEqualTo("");
        assertThat(address.getCountry()).isEqualTo("");
        assertThat(address.getPostalCode()).isEqualTo("");
    }
}

