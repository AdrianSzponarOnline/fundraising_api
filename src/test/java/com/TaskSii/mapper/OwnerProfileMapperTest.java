package com.TaskSii.mapper;

import com.TaskSii.dto.OwnerDTO;
import com.TaskSii.dto.RegisterOwnerDTO;
import com.TaskSii.model.OwnerProfile;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerProfileMapperTest {
    private final OwnerProfileMapper mapper = Mappers.getMapper(OwnerProfileMapper.class);

    @Test
    void shouldMapDTOToOwnerProfile() {
        RegisterOwnerDTO dto = new RegisterOwnerDTO(
                "test@test.pl",
                "secret1",
                "Test Org",
                "1234567890",
                "123456789",
                "1234567890",
                "691111956",
                null
        );

        OwnerProfile entity = mapper.fromRegisterOwnerDTO(dto);

        assertThat(entity).isNotNull();
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull(); // ignorowane
        assertThat(entity.getUser()).isNull(); // ignorowane
        assertThat(entity.getAddresses()).isEmpty();
        assertThat(entity.getFundraisingEvents()).isEmpty();
        assertThat(entity.getVolunteers()).isEmpty();

        assertThat(entity.getEmail()).isEqualTo(dto.email());
        assertThat(entity.getOrganizationName()).isEqualTo(dto.organizationName());
        assertThat(entity.getNip()).isEqualTo(dto.nip());
        assertThat(entity.getRegon()).isEqualTo(dto.regon());
        assertThat(entity.getKrs()).isEqualTo(dto.krs());
        assertThat(entity.getPhoneNumber()).isEqualTo(dto.phoneNumber());


        assertThat(entity.getUser()).isNull();
    }

    @Test
    void shouldMapEntityToDTO() {
        OwnerProfile ownerProfile = OwnerProfile.builder()
                .id(1L)
                .email("test@test.pl")
                .organizationName("Test Org")
                .nip("1234567890")
                .regon("123456789")
                .krs("1234567890")
                .phoneNumber("691111956")
                .addresses(List.of())
                .build();

        OwnerDTO dto = mapper.toDto(ownerProfile);

        assertThat(dto).isNotNull();
        assertThat(dto.email()).isEqualTo(ownerProfile.getEmail());
        assertThat(dto.organizationName()).isEqualTo(ownerProfile.getOrganizationName());
        assertThat(dto.nip()).isEqualTo(ownerProfile.getNip());
        assertThat(dto.regon()).isEqualTo(ownerProfile.getRegon());
        assertThat(dto.krs()).isEqualTo(ownerProfile.getKrs());
        assertThat(dto.phoneNumber()).isEqualTo(ownerProfile.getPhoneNumber());

        assertThat(dto.addresses()).isNotNull();
        assertThat(dto.addresses()).isEmpty();
    }

    @Test
    void shouldMapDTOToOwnerProfileWithNullValues() {
        RegisterOwnerDTO dto = new RegisterOwnerDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        OwnerProfile entity = mapper.fromRegisterOwnerDTO(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getUser()).isNull();
        assertThat(entity.getAddresses()).isEmpty();
        assertThat(entity.getFundraisingEvents()).isEmpty();
        assertThat(entity.getVolunteers()).isEmpty();
        assertThat(entity.getEmail()).isNull();
        assertThat(entity.getOrganizationName()).isNull();
        assertThat(entity.getNip()).isNull();
        assertThat(entity.getRegon()).isNull();
        assertThat(entity.getKrs()).isNull();
        assertThat(entity.getPhoneNumber()).isNull();
    }

    @Test
    void shouldMapEntityToDTOWithNullValues() {
        OwnerProfile ownerProfile = OwnerProfile.builder()
                .id(null)
                .email(null)
                .organizationName(null)
                .nip(null)
                .regon(null)
                .krs(null)
                .phoneNumber(null)
                .addresses(null)
                .build();

        OwnerDTO dto = mapper.toDto(ownerProfile);

        assertThat(dto).isNotNull();
        assertThat(dto.email()).isNull();
        assertThat(dto.organizationName()).isNull();
        assertThat(dto.nip()).isNull();
        assertThat(dto.regon()).isNull();
        assertThat(dto.krs()).isNull();
        assertThat(dto.phoneNumber()).isNull();
        assertThat(dto.addresses()).isNull();
    }

    @Test
    void shouldMapDTOToOwnerProfileWithEmptyStrings() {
        RegisterOwnerDTO dto = new RegisterOwnerDTO(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                null
        );

        OwnerProfile entity = mapper.fromRegisterOwnerDTO(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getUser()).isNull();
        assertThat(entity.getAddresses()).isEmpty();
        assertThat(entity.getFundraisingEvents()).isEmpty();
        assertThat(entity.getVolunteers()).isEmpty();
        assertThat(entity.getEmail()).isEqualTo("");
        assertThat(entity.getOrganizationName()).isEqualTo("");
        assertThat(entity.getNip()).isEqualTo("");
        assertThat(entity.getRegon()).isEqualTo("");
        assertThat(entity.getKrs()).isEqualTo("");
        assertThat(entity.getPhoneNumber()).isEqualTo("");
    }

    @Test
    void shouldMapEntityToDTOWithEmptyStrings() {
        OwnerProfile ownerProfile = OwnerProfile.builder()
                .id(1L)
                .email("")
                .organizationName("")
                .nip("")
                .regon("")
                .krs("")
                .phoneNumber("")
                .addresses(List.of())
                .build();

        OwnerDTO dto = mapper.toDto(ownerProfile);

        assertThat(dto).isNotNull();
        assertThat(dto.email()).isEqualTo("");
        assertThat(dto.organizationName()).isEqualTo("");
        assertThat(dto.nip()).isEqualTo("");
        assertThat(dto.regon()).isEqualTo("");
        assertThat(dto.krs()).isEqualTo("");
        assertThat(dto.phoneNumber()).isEqualTo("");
        assertThat(dto.addresses()).isNotNull();
        assertThat(dto.addresses()).isEmpty();
    }
}
