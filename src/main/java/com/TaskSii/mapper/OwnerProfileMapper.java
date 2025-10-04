package com.TaskSii.mapper;

import com.TaskSii.dto.OwnerDTO;
import com.TaskSii.dto.RegisterOwnerDTO;
import com.TaskSii.model.OwnerProfile;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface OwnerProfileMapper {

    @Mapping(target = "fundraisingEvents", ignore = true)
    @Mapping(target = "volunteers", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    OwnerProfile fromRegisterOwnerDTO(RegisterOwnerDTO dto);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "organizationName", source = "organizationName")
    OwnerDTO toDto(OwnerProfile ownerProfile);

}
