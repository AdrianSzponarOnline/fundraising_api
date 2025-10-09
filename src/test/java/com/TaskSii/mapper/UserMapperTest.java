package com.TaskSii.mapper;

import com.TaskSii.dto.RegisterRequestDTO;
import com.TaskSii.dto.UserDto;
import com.TaskSii.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapUserToUserDto() {
        User user = User.builder()
                .user_id(1L)
                .email("test@example.com")
                .enabled(true)
                .build();

        UserDto dto = mapper.toUserDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.email()).isEqualTo(user.getEmail());
    }

    @Test
    void shouldMapRegisterRequestDTOToUser() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "newuser@example.com",
                "Password123"
        );

        User user = mapper.fromRegisterRequestDTO(dto);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(dto.email());
        assertThat(user.getPassword()).isNull(); // ignored in mapping
        assertThat(user.isEnabled()).isTrue(); // constant value
        assertThat(user.getUser_id()).isNull(); // ignored in mapping
        assertThat(user.getRoles()).isNull(); // ignored in mapping
        assertThat(user.getOwnerProfile()).isNull(); // ignored in mapping
        assertThat(user.getVolunteer()).isNull(); // ignored in mapping
    }

    @Test
    void shouldMapUserToUserDtoWithNullValues() {
        User user = User.builder()
                .user_id(null)
                .email(null)
                .enabled(false)
                .build();

        UserDto dto = mapper.toUserDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.email()).isNull();
    }

    @Test
    void shouldMapRegisterRequestDTOToUserWithNullValues() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                null,
                null
        );

        User user = mapper.fromRegisterRequestDTO(dto);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.isEnabled()).isTrue(); // constant value
        assertThat(user.getUser_id()).isNull();
        assertThat(user.getRoles()).isNull();
        assertThat(user.getOwnerProfile()).isNull();
        assertThat(user.getVolunteer()).isNull();
    }
}
