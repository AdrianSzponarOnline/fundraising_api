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
}

