package com.TaskSii.mapper;

import com.TaskSii.dto.collectionbox.CollectionBoxDTO;
import com.TaskSii.model.CollectionBox;
import com.TaskSii.model.FundraisingEvent;
import com.TaskSii.model.Volunteer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionBoxMapperTest {

    private final CollectionBoxMapper mapper = Mappers.getMapper(CollectionBoxMapper.class);

    @Test
    void shouldMapCollectionBoxToDTOWhenAssigned(){
        FundraisingEvent event = new FundraisingEvent();
        Volunteer volunteer = new Volunteer();
        CollectionBox collectionBox = CollectionBox.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .fundraisingEvent(event)
                .volunteer(volunteer)
                .empty(false)
                .build();

        CollectionBoxDTO dto = mapper.toDTO(collectionBox);

        assertThat(dto.id()).isEqualTo(collectionBox.getId());
        assertThat(dto.empty()).isEqualTo(collectionBox.isEmpty());
        assertThat(dto.assigned()).isTrue();
    }

    @Test
    void shouldMapCollectionBoxToDTOWhenUnassigned(){
        Volunteer volunteer = new Volunteer();
        CollectionBox collectionBox = CollectionBox.builder()
                .id(2L)
                .created(LocalDateTime.now())
                .volunteer(volunteer)
                .empty(true)
                .fundraisingEvent(null)
                .build();

        CollectionBoxDTO dto = mapper.toDTO(collectionBox);

        assertThat(dto.id()).isEqualTo(collectionBox.getId());
        assertThat(dto.empty()).isEqualTo(collectionBox.isEmpty());
        assertThat(dto.assigned()).isFalse();
    }

    @Test
    void shouldMapCollectionBoxToDTOWithEventAndVolunteerDetails() {
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
                .id(3L)
                .created(LocalDateTime.now())
                .fundraisingEvent(event)
                .volunteer(volunteer)
                .empty(false)
                .build();

        CollectionBoxDTO dto = mapper.toDTO(collectionBox);

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
    void shouldMapCollectionBoxToDTOWithNullVolunteer() {
        FundraisingEvent event = FundraisingEvent.builder()
                .id(100L)
                .name("Test Event")
                .build();
        
        CollectionBox collectionBox = CollectionBox.builder()
                .id(4L)
                .created(LocalDateTime.now())
                .fundraisingEvent(event)
                .volunteer(null)
                .empty(true)
                .build();

        CollectionBoxDTO dto = mapper.toDTO(collectionBox);

        assertThat(dto.id()).isEqualTo(collectionBox.getId());
        assertThat(dto.empty()).isEqualTo(collectionBox.isEmpty());
        assertThat(dto.assigned()).isTrue();
        assertThat(dto.eventId()).isEqualTo(100L);
        assertThat(dto.eventName()).isEqualTo("Test Event");
        assertThat(dto.volunteerId()).isNull();
        assertThat(dto.volunteerFirstName()).isNull();
        assertThat(dto.volunteerLastName()).isNull();
    }

    @Test
    void shouldMapCollectionBoxListToDTOList() {
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

        java.util.List<CollectionBox> boxes = java.util.List.of(box1, box2);
        java.util.List<CollectionBoxDTO> dtos = mapper.toDTO(boxes);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).id()).isEqualTo(1L);
        assertThat(dtos.get(0).assigned()).isFalse();
        assertThat(dtos.get(1).id()).isEqualTo(2L);
        assertThat(dtos.get(1).assigned()).isTrue();
        assertThat(dtos.get(1).eventId()).isEqualTo(100L);
        assertThat(dtos.get(1).volunteerId()).isEqualTo(200L);
    }

}
