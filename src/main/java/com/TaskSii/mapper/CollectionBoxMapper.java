package com.TaskSii.mapper;

import com.TaskSii.dto.collectionbox.CollectionBoxDTO;
import com.TaskSii.model.CollectionBox;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface CollectionBoxMapper {

    @Mapping(target = "assigned", expression = "java(collectionBox.getFundraisingEvent() != null)")
    @Mapping(target = "eventId", expression = "java(collectionBox.getFundraisingEvent() != null ? collectionBox.getFundraisingEvent().getId() : null)")
    @Mapping(target = "eventName", expression = "java(collectionBox.getFundraisingEvent() != null ? collectionBox.getFundraisingEvent().getName() : null)")
    @Mapping(target = "volunteerId", expression = "java(collectionBox.getVolunteer() != null ? collectionBox.getVolunteer().getId() : null)")
    @Mapping(target = "volunteerFirstName", expression = "java(collectionBox.getVolunteer() != null ? collectionBox.getVolunteer().getFirstName() : null)")
    @Mapping(target = "volunteerLastName", expression = "java(collectionBox.getVolunteer() != null ? collectionBox.getVolunteer().getLastName() : null)")
    CollectionBoxDTO toDTO(CollectionBox collectionBox);


    List<CollectionBoxDTO> toDTO(List<CollectionBox> collectionBoxes);
}
