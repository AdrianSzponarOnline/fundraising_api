package com.TaskSii.mapper;

import com.TaskSii.dto.VolunteerCreateDTO;
import com.TaskSii.dto.VolunteerResponseDTO;
import com.TaskSii.dto.VolunteerUpdateDTO;
import com.TaskSii.dto.collectionbox.CollectionBoxDTO;
import com.TaskSii.model.Volunteer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VolunteerMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ownerProfile", ignore = true)
    @Mapping(target = "collectionBox", ignore = true)
    Volunteer toEntity(VolunteerCreateDTO dto);

    @Mapping(source = "ownerProfile.id", target = "ownerProfileId")
    VolunteerResponseDTO toDto(Volunteer volunteer);




    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "collectionBox", ignore = true)
    @Mapping(target = "ownerProfile", ignore = true)
    void updateVolunteerFromDto(VolunteerUpdateDTO dto, @MappingTarget Volunteer volunteer);
}
