package com.TaskSii.mapper;

import com.TaskSii.dto.CollectionBoxDTO;
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

}
