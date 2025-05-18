package com.TaskSii.dto;

public class CollectionBoxDTO {
    private Long id;
    private boolean empty;
    private boolean assigned;

    public CollectionBoxDTO() {
    }

    public CollectionBoxDTO(Long id, boolean empty, boolean assigned) {
        this.assigned = assigned;
        this.id = id;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
