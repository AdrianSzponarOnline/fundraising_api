package com.TaskSii.mapper;

import com.TaskSii.dto.CreateFundraisingEventDTO;
import com.TaskSii.dto.FinancialReportDTO;
import com.TaskSii.dto.FundraisingEventDTO;
import com.TaskSii.model.FundraisingEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring", uses = CollectionBoxMapper.class)
public interface FundraisingEventMapper {

    FundraisingEventDTO toDTO(FundraisingEvent event);
    @Mapping(target = "ownerProfile", ignore = true)
    FundraisingEvent toEntity (FundraisingEventDTO dto);

    @Mapping(source = "name", target = "eventName")
    CreateFundraisingEventDTO toCreateDto(FundraisingEvent event);

    @Mapping(target = "eventName", source = "name")
    @Mapping(target = "amount", source = "accountBalance")
    FinancialReportDTO toFinancialReport(FundraisingEvent event);

    List<FundraisingEventDTO> toFundraisingEventList(List<FundraisingEvent> dtoList);
}
