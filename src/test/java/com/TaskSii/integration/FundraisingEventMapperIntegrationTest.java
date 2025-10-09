package com.TaskSii.integration;

import com.TaskSii.dto.FundraisingEventDTO;
import com.TaskSii.mapper.FundraisingEventMapper;
import com.TaskSii.model.Currency;
import com.TaskSii.model.FundraisingEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class FundraisingEventMapperIntegrationTest {

    @Autowired
    private FundraisingEventMapper mapper;

    @Test
    void shouldMapToDTOWithSpringContext() {
        FundraisingEvent event = FundraisingEvent.builder()
                .id(1L)
                .name("Test Event")
                .currency(Currency.PLN)
                .accountBalance(BigDecimal.valueOf(100))
                .collectionBoxes(null) // This will be handled by CollectionBoxMapper
                .build();

        FundraisingEventDTO dto = mapper.toDTO(event);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(event.getId());
        assertThat(dto.name()).isEqualTo(event.getName());
        assertThat(dto.currency()).isEqualTo(event.getCurrency());
        assertThat(dto.accountBalance()).isEqualTo(event.getAccountBalance());
        assertThat(dto.collectionBoxes()).isNull(); // Should be null since collectionBoxes is null
    }

    @Test
    void shouldMapToFundraisingEventListWithSpringContext() {
        FundraisingEvent event1 = FundraisingEvent.builder()
                .id(1L)
                .name("Event 1")
                .currency(Currency.PLN)
                .accountBalance(BigDecimal.valueOf(100))
                .collectionBoxes(null)
                .build();

        FundraisingEvent event2 = FundraisingEvent.builder()
                .id(2L)
                .name("Event 2")
                .currency(Currency.USD)
                .accountBalance(BigDecimal.valueOf(200))
                .collectionBoxes(null)
                .build();

        List<FundraisingEvent> events = List.of(event1, event2);
        List<FundraisingEventDTO> dtos = mapper.toFundraisingEventList(events);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).id()).isEqualTo(1L);
        assertThat(dtos.get(0).name()).isEqualTo("Event 1");
        assertThat(dtos.get(0).currency()).isEqualTo(Currency.PLN);
        assertThat(dtos.get(1).id()).isEqualTo(2L);
        assertThat(dtos.get(1).name()).isEqualTo("Event 2");
        assertThat(dtos.get(1).currency()).isEqualTo(Currency.USD);
    }

    @Test
    void shouldMapToDTOWithEmptyCollectionBoxes() {
        FundraisingEvent event = FundraisingEvent.builder()
                .id(1L)
                .name("Test Event")
                .currency(Currency.PLN)
                .accountBalance(BigDecimal.valueOf(100))
                .collectionBoxes(List.of()) // Empty list
                .build();

        FundraisingEventDTO dto = mapper.toDTO(event);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(event.getId());
        assertThat(dto.name()).isEqualTo(event.getName());
        assertThat(dto.currency()).isEqualTo(event.getCurrency());
        assertThat(dto.accountBalance()).isEqualTo(event.getAccountBalance());
        assertThat(dto.collectionBoxes()).isNotNull();
        assertThat(dto.collectionBoxes()).isEmpty();
    }

    @Test
    void shouldMapToFundraisingEventListWithEmptyList() {
        List<FundraisingEvent> events = List.of();
        List<FundraisingEventDTO> dtos = mapper.toFundraisingEventList(events);

        assertThat(dtos).isEmpty();
    }

    @Test
    void shouldMapToDTOWithNullValues() {
        FundraisingEvent event = FundraisingEvent.builder()
                .id(null)
                .name(null)
                .currency(null)
                .accountBalance(null)
                .collectionBoxes(null)
                .build();

        FundraisingEventDTO dto = mapper.toDTO(event);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isNull();
        assertThat(dto.name()).isNull();
        assertThat(dto.currency()).isNull();
        assertThat(dto.accountBalance()).isNull();
        assertThat(dto.collectionBoxes()).isNull();
    }
}
