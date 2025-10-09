package com.TaskSii.integration;

import com.TaskSii.dto.AddressDTO;
import com.TaskSii.dto.FundraisingEventDTO;
import com.TaskSii.dto.RegisterRequestDTO;
import com.TaskSii.dto.UserDto;
import com.TaskSii.dto.VolunteerCreateDTO;
import com.TaskSii.dto.VolunteerResponseDTO;
import com.TaskSii.dto.VolunteerUpdateDTO;
import com.TaskSii.dto.collectionbox.CollectionBoxDTO;
import com.TaskSii.mapper.AddressMapper;
import com.TaskSii.mapper.CollectionBoxMapper;
import com.TaskSii.mapper.FundraisingEventMapper;
import com.TaskSii.mapper.OwnerProfileMapper;
import com.TaskSii.mapper.UserMapper;
import com.TaskSii.mapper.VolunteerMapper;
import com.TaskSii.model.Address;
import com.TaskSii.model.CollectionBox;
import com.TaskSii.model.Currency;
import com.TaskSii.model.FundraisingEvent;
import com.TaskSii.model.OwnerProfile;
import com.TaskSii.model.User;
import com.TaskSii.model.Volunteer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class MapperIntegrationTest {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private CollectionBoxMapper collectionBoxMapper;

    @Autowired
    private FundraisingEventMapper fundraisingEventMapper;

    @Autowired
    private OwnerProfileMapper ownerProfileMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VolunteerMapper volunteerMapper;

    @Test
    void shouldMapAddressWithSpringContext() {
        Address address = Address.builder()
                .id(1L)
                .streetName("Test Street")
                .city("Lublin")
                .state("Lubelskie")
                .country("Polska")
                .postalCode("20-001")
                .build();

        AddressDTO dto = addressMapper.toAddressDTO(address);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(address.getId());
        assertThat(dto.streetName()).isEqualTo(address.getStreetName());
        assertThat(dto.city()).isEqualTo(address.getCity());
        assertThat(dto.state()).isEqualTo(address.getState());
        assertThat(dto.country()).isEqualTo(address.getCountry());
        assertThat(dto.postalCode()).isEqualTo(address.getPostalCode());
    }

    @Test
    void shouldMapCollectionBoxWithSpringContext() {
        FundraisingEvent event = FundraisingEvent.builder()
                .id(100L)
                .name("Test Event")
                .build();

        Volunteer volunteer = Volunteer.builder()
                .id(200L)
                .firstName("John")
                .lastName("Doe")
                .build();

        CollectionBox collectionBox = CollectionBox.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .fundraisingEvent(event)
                .volunteer(volunteer)
                .empty(false)
                .build();

        CollectionBoxDTO dto = collectionBoxMapper.toDTO(collectionBox);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(collectionBox.getId());
        assertThat(dto.empty()).isEqualTo(collectionBox.isEmpty());
        assertThat(dto.assigned()).isTrue();
        assertThat(dto.eventId()).isEqualTo(100L);
        assertThat(dto.eventName()).isEqualTo("Test Event");
        assertThat(dto.volunteerId()).isEqualTo(200L);
        assertThat(dto.volunteerFirstName()).isEqualTo("John");
        assertThat(dto.volunteerLastName()).isEqualTo("Doe");
    }

    @Test
    void shouldMapFundraisingEventWithSpringContext() {
        FundraisingEvent event = FundraisingEvent.builder()
                .id(1L)
                .name("Test Event")
                .currency(Currency.PLN)
                .accountBalance(BigDecimal.valueOf(100))
                .collectionBoxes(List.of())
                .build();

        FundraisingEventDTO dto = fundraisingEventMapper.toDTO(event);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(event.getId());
        assertThat(dto.name()).isEqualTo(event.getName());
        assertThat(dto.currency()).isEqualTo(event.getCurrency());
        assertThat(dto.accountBalance()).isEqualTo(event.getAccountBalance());
        assertThat(dto.collectionBoxes()).isNotNull();
        assertThat(dto.collectionBoxes()).isEmpty();
    }

    @Test
    void shouldMapUserWithSpringContext() {
        User user = User.builder()
                .user_id(1L)
                .email("test@example.com")
                .enabled(true)
                .build();

        UserDto dto = userMapper.toUserDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.email()).isEqualTo(user.getEmail());
    }

    @Test
    void shouldMapVolunteerWithSpringContext() {
        OwnerProfile ownerProfile = OwnerProfile.builder()
                .id(100L)
                .organizationName("Test Org")
                .build();

        Volunteer volunteer = Volunteer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@test.pl")
                .phoneNumber("+48123123123")
                .password("password123")
                .ownerProfile(ownerProfile)
                .collectionBox(null)
                .build();

        VolunteerResponseDTO dto = volunteerMapper.toDto(volunteer);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(volunteer.getId());
        assertThat(dto.firstName()).isEqualTo(volunteer.getFirstName());
        assertThat(dto.lastName()).isEqualTo(volunteer.getLastName());
        assertThat(dto.email()).isEqualTo(volunteer.getEmail());
        assertThat(dto.phoneNumber()).isEqualTo(volunteer.getPhoneNumber());
        assertThat(dto.ownerProfileId()).isEqualTo(100L);
        assertThat(dto.collectionBoxes()).isNull();
    }

    @Test
    void shouldMapVolunteerCreateDTOWithSpringContext() {
        VolunteerCreateDTO dto = new VolunteerCreateDTO(
                "John",
                "Doe",
                "john@test.pl",
                "+48123123123",
                "password123",
                1L
        );

        Volunteer volunteer = volunteerMapper.toEntity(dto);

        assertThat(volunteer).isNotNull();
        assertThat(volunteer.getFirstName()).isEqualTo(dto.firstName());
        assertThat(volunteer.getLastName()).isEqualTo(dto.lastName());
        assertThat(volunteer.getEmail()).isEqualTo(dto.email());
        assertThat(volunteer.getPhoneNumber()).isEqualTo(dto.phoneNumber());
        assertThat(volunteer.getPassword()).isEqualTo(dto.password());
        assertThat(volunteer.getUser()).isNull();
        assertThat(volunteer.getOwnerProfile()).isNull();
        assertThat(volunteer.getCollectionBox()).isNull();
    }

    @Test
    void shouldMapRegisterRequestDTOWithSpringContext() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "newuser@example.com",
                "Password123"
        );

        User user = userMapper.fromRegisterRequestDTO(dto);

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
    void shouldMapCollectionBoxListWithSpringContext() {
        CollectionBox box1 = CollectionBox.builder()
                .id(1L)
                .empty(true)
                .fundraisingEvent(null)
                .volunteer(null)
                .build();

        CollectionBox box2 = CollectionBox.builder()
                .id(2L)
                .empty(false)
                .fundraisingEvent(FundraisingEvent.builder().id(100L).name("Event").build())
                .volunteer(Volunteer.builder().id(200L).firstName("John").lastName("Doe").build())
                .build();

        List<CollectionBox> boxes = List.of(box1, box2);
        List<CollectionBoxDTO> dtos = collectionBoxMapper.toDTO(boxes);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).id()).isEqualTo(1L);
        assertThat(dtos.get(0).assigned()).isFalse();
        assertThat(dtos.get(1).id()).isEqualTo(2L);
        assertThat(dtos.get(1).assigned()).isTrue();
        assertThat(dtos.get(1).eventId()).isEqualTo(100L);
        assertThat(dtos.get(1).volunteerId()).isEqualTo(200L);
    }
}
