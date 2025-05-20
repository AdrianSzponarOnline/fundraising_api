package com.TaskSii.dto;

import jakarta.validation.constraints.NotNull;

public class AssignBoxDTO {

    @NotNull(message = "Event id cannot be null")
    private Long eventId;

    public AssignBoxDTO() {
    }

    public AssignBoxDTO(Long eventId) {
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
