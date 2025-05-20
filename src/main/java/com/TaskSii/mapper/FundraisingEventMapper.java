package com.TaskSii.mapper;

import com.TaskSii.dto.CreateFundraisingEventDTO;
import com.TaskSii.dto.FundraisingEventDTO;
import com.TaskSii.model.FundraisingEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface FundraisingEventMapper {

    FundraisingEventDTO toDTO(FundraisingEvent event);
    FundraisingEvent toEntity (FundraisingEventDTO dto);

    @Mapping(source = "name", target = "eventName")
    CreateFundraisingEventDTO toCreateDto(FundraisingEvent event);
}
