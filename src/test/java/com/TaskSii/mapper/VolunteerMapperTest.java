package com.TaskSii.mapper;

import com.TaskSii.dto.VolunteerCreateDTO;
import com.TaskSii.dto.VolunteerUpdateDTO;
import com.TaskSii.model.Volunteer;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
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
        assertThat(entity.getCollectionBoxes()).isEmpty();
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

}
