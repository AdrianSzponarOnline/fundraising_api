package com.TaskSii.dto;

import java.util.List;

public record VolunteerResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Long ownerProfileId,
        List<CollectionBoxDTO> collectionBoxes
) {
}
