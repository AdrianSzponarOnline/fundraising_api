package com.TaskSii.mapper;

import com.TaskSii.dto.collectionbox.CollectionBoxDTO;
import com.TaskSii.model.CollectionBox;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface CollectionBoxMapper {

    @Mapping(target = "assigned", expression="java(collectionBox.getFundraisingEvent() != null)")
    CollectionBoxDTO toDTO(CollectionBox collectionBox);

    List<CollectionBoxDTO> toDTO(List<CollectionBox> collectionBoxes);
}
