package com.TaskSii.mapper;

import com.TaskSii.dto.VolunteerCreateDTO;
import com.TaskSii.dto.VolunteerResponseDTO;
import com.TaskSii.dto.VolunteerUpdateDTO;
import com.TaskSii.model.Volunteer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VolunteerMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ownerProfile", ignore = true)
    @Mapping(target = "collectionBoxes", ignore = true)
    Volunteer toEntity(VolunteerCreateDTO dto);

    @Mapping(source = "ownerProfile.id", target = "ownerProfileId")
    @Mapping(target = "collectionBoxes", qualifiedByName = "mapCollectionBoxes")
    VolunteerResponseDTO toDto(Volunteer volunteer);

    @Named("mapCollectionBoxes")
    default java.util.List<com.TaskSii.dto.CollectionBoxDTO> mapCollectionBoxes(java.util.List<com.TaskSii.model.CollectionBox> boxes) {
        if (boxes == null) return java.util.Collections.emptyList();
        java.util.List<com.TaskSii.dto.CollectionBoxDTO> result = new java.util.ArrayList<>();
        for (com.TaskSii.model.CollectionBox box : boxes) {
            boolean assigned = box.getFundraisingEvent() != null;
            result.add(new com.TaskSii.dto.CollectionBoxDTO(
                    box.getId(),
                    box.isEmpty(),
                    assigned
            ));
        }
        return result;
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "collectionBoxes", ignore = true)
    @Mapping(target = "ownerProfile", ignore = true)
    void updateVolunteerFromDto(VolunteerUpdateDTO dto, @MappingTarget Volunteer volunteer);
}
