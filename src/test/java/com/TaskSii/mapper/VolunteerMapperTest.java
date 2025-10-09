package com.TaskSii.mapper;

import com.TaskSii.dto.VolunteerCreateDTO;
import com.TaskSii.dto.VolunteerUpdateDTO;
import com.TaskSii.model.Volunteer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class VolunteerMapperTest {

    private final VolunteerMapper mapper = Mappers.getMapper(VolunteerMapper.class);

    @Test
    void shouldMapCreateDTOToEntity() {
        VolunteerCreateDTO dto = new VolunteerCreateDTO(
                "Jan",
                "Kowalski",
                "jan.kowalski@test.pl",
                "+48123123123",
                "secret123",
                1L
        );

        Volunteer entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getFirstName()).isEqualTo(dto.firstName());
        assertThat(entity.getLastName()).isEqualTo(dto.lastName());
        assertThat(entity.getEmail()).isEqualTo(dto.email());
        assertThat(entity.getPhoneNumber()).isEqualTo(dto.phoneNumber());
        assertThat(entity.getPassword()).isEqualTo(dto.password());

        assertThat(entity.getUser()).isNull();
        assertThat(entity.getOwnerProfile()).isNull();
        assertThat(entity.getCollectionBox()).isNull();
    }

    @Test
    void shouldUpdateEntityFromUpdateDto() {
        Volunteer entity = Volunteer.builder()
                .id(200L)
                .firstName("OldFirst")
                .lastName("OldLast")
                .email("old@test.pl")
                .phoneNumber("12345")
                .password("oldpass")
                .build();

        VolunteerUpdateDTO dto = new VolunteerUpdateDTO(
                "NewFirst",
                null,
                "new@test.pl",
                null,
                "newpass"
        );

        mapper.updateVolunteerFromDto(dto, entity);

        assertThat(entity.getFirstName()).isEqualTo("NewFirst");
        assertThat(entity.getLastName()).isEqualTo("OldLast"); // nie nadpisane null
        assertThat(entity.getEmail()).isEqualTo("new@test.pl");
        assertThat(entity.getPhoneNumber()).isEqualTo("12345"); // nie nadpisane null
        assertThat(entity.getPassword()).isEqualTo("newpass");
    }

    @Test
    void shouldMapEntityToResponseDto() {
        Volunteer volunteer = Volunteer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@test.pl")
                .phoneNumber("+48123123123")
                .password("password123")
                .ownerProfile(com.TaskSii.model.OwnerProfile.builder().id(100L).build())
                .collectionBox(null)
                .build();

        com.TaskSii.dto.VolunteerResponseDTO dto = mapper.toDto(volunteer);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(volunteer.getId());
        assertThat(dto.firstName()).isEqualTo(volunteer.getFirstName());
        assertThat(dto.lastName()).isEqualTo(volunteer.getLastName());
        assertThat(dto.email()).isEqualTo(volunteer.getEmail());
        assertThat(dto.phoneNumber()).isEqualTo(volunteer.getPhoneNumber());
        assertThat(dto.ownerProfileId()).isEqualTo(100L);
        assertThat(dto.collectionBoxes()).isNull(); // collectionBox is null, so collectionBoxes should be null
    }

    @Test
    void shouldMapEntityToResponseDtoWithNullOwnerProfile() {
        Volunteer volunteer = Volunteer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@test.pl")
                .phoneNumber("+48123123123")
                .password("password123")
                .ownerProfile(null)
                .collectionBox(null)
                .build();

        com.TaskSii.dto.VolunteerResponseDTO dto = mapper.toDto(volunteer);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(volunteer.getId());
        assertThat(dto.firstName()).isEqualTo(volunteer.getFirstName());
        assertThat(dto.lastName()).isEqualTo(volunteer.getLastName());
        assertThat(dto.email()).isEqualTo(volunteer.getEmail());
        assertThat(dto.phoneNumber()).isEqualTo(volunteer.getPhoneNumber());
        assertThat(dto.ownerProfileId()).isNull();
        assertThat(dto.collectionBoxes()).isNull(); // collectionBox is null, so collectionBoxes should be null
    }

    @Test
    void shouldUpdateEntityFromUpdateDtoWithAllNullValues() {
        Volunteer entity = Volunteer.builder()
                .id(200L)
                .firstName("OldFirst")
                .lastName("OldLast")
                .email("old@test.pl")
                .phoneNumber("12345")
                .password("oldpass")
                .build();

        VolunteerUpdateDTO dto = new VolunteerUpdateDTO(
                null,
                null,
                null,
                null,
                null
        );

        mapper.updateVolunteerFromDto(dto, entity);

        // All values should remain unchanged due to null value property mapping strategy
        assertThat(entity.getFirstName()).isEqualTo("OldFirst");
        assertThat(entity.getLastName()).isEqualTo("OldLast");
        assertThat(entity.getEmail()).isEqualTo("old@test.pl");
        assertThat(entity.getPhoneNumber()).isEqualTo("12345");
        assertThat(entity.getPassword()).isEqualTo("oldpass");
    }

    @Test
    void shouldMapCreateDTOToEntityWithNullValues() {
        VolunteerCreateDTO dto = new VolunteerCreateDTO(
                null,
                null,
                null,
                null,
                null,
                null
        );

        Volunteer entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getFirstName()).isNull();
        assertThat(entity.getLastName()).isNull();
        assertThat(entity.getEmail()).isNull();
        assertThat(entity.getPhoneNumber()).isNull();
        assertThat(entity.getPassword()).isNull();
        // OwnerProfileId is not a field in Volunteer model
        assertThat(entity.getUser()).isNull();
        assertThat(entity.getOwnerProfile()).isNull();
        assertThat(entity.getCollectionBox()).isNull();
    }

}
