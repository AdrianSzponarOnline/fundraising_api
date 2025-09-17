package com.TaskSii.mapper;

import com.TaskSii.dto.OwnerDTO;
import com.TaskSii.dto.RegisterOwnerDTO;
import com.TaskSii.model.OwnerProfile;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OwnerProfileMapper {

    OwnerProfileMapper INSTANCE = Mappers.getMapper(OwnerProfileMapper.class);

    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "fundraisingEvents", ignore = true)
    @Mapping(target = "volunteers", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    OwnerProfile fromRegisterOwnerDTO(RegisterOwnerDTO dto);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "organizatioName", source = "organizationName")
    OwnerDTO toDto(OwnerProfile ownerProfile);

}
