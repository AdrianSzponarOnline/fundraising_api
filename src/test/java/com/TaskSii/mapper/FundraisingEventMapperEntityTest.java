package com.TaskSii.mapper;

import com.TaskSii.dto.CreateFundraisingEventDTO;
import com.TaskSii.dto.FinancialReportDTO;
import com.TaskSii.dto.FundraisingEventDTO;
import com.TaskSii.model.Currency;
import com.TaskSii.model.FundraisingEvent;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class FundraisingEventMapperEntityTest {

    private final FundraisingEventMapper mapper = Mappers.getMapper(FundraisingEventMapper.class);

    @Test
    void shouldMapDTOToEntity() {
        FundraisingEventDTO dto = new FundraisingEventDTO(
                1L,
                "Test Event",
                Currency.PLN,
                BigDecimal.valueOf(500)
        );

        FundraisingEvent entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isEqualTo(dto.name());
        assertThat(entity.getCurrency()).isEqualTo(dto.currency());
        assertThat(entity.getAccountBalance()).isEqualTo(dto.accountBalance());
        assertThat(entity.getOwnerProfile()).isNull();
    }
    @Test
    void shouldMapToDTO() {
        FundraisingEvent event = FundraisingEvent.builder()
                .id(1L)
                .name("Test Event")
                .currency(Currency.PLN)
                .accountBalance(BigDecimal.valueOf(100))
                .build();

        FundraisingEventDTO dto = mapper.toDTO(event);

        assertThat(dto.id()).isEqualTo(event.getId());
        assertThat(dto.name()).isEqualTo(event.getName());
        assertThat(dto.currency()).isEqualTo(event.getCurrency());
        assertThat(dto.accountBalance()).isEqualTo(event.getAccountBalance());
    }

    @Test
    void shouldMapToCreateDTO() {
        FundraisingEvent event = FundraisingEvent.builder()
                .name("Event 2")
                .build();

        CreateFundraisingEventDTO createDTO = mapper.toCreateDto(event);

        assertThat(createDTO.eventName()).isEqualTo(event.getName());
    }

    @Test
    void shouldMapToFinancialReport() {
        FundraisingEvent event = FundraisingEvent.builder()
                .name("Event 3")
                .accountBalance(BigDecimal.valueOf(250))
                .build();

        FinancialReportDTO reportDTO = mapper.toFinancialReport(event);

        assertThat(reportDTO.eventName()).isEqualTo(event.getName());
        assertThat(reportDTO.amount()).isEqualTo(event.getAccountBalance());
    }
}
