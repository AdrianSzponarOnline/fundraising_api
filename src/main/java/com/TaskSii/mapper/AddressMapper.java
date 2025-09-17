package com.TaskSii.mapper;

import com.TaskSii.dto.AddressDTO;
import com.TaskSii.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDTO toAddressDTO(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Address fromAddressDTO(AddressDTO addressDTO);
}
