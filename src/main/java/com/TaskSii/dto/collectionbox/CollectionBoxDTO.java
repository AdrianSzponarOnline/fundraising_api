package com.TaskSii.dto.collectionbox;

import java.time.LocalDateTime;

public record CollectionBoxDTO(
        Long id,
        boolean empty,
        boolean assigned,
        LocalDateTime created,
        LocalDateTime collectedAt,
        Long eventId,
        String eventName,
        Long volunteerId,
        String volunteerFirstName,
        String volunteerLastName)
{
}
