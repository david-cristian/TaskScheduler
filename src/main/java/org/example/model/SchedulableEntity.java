package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
public abstract class SchedulableEntity extends Entity<Long> {
    @Column(nullable = false)
    protected String title;

    @Column(nullable = false)
    protected LocalDate dueDate;

    @Column(nullable = true)
    protected String description;

    @Column(nullable = false)
    protected Status status;

    protected SchedulableEntity() {
        super();
    }

    protected SchedulableEntity(String title, LocalDate dueDate, String description, Status status) {
        super();
        this.title = title;
        this.dueDate = dueDate;
        this.description = description;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
