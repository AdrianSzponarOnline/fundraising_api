package com.TaskSii.dto;

public class AssignBoxDTO {
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
