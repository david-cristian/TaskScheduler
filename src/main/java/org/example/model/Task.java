package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "tasks")
public class Task extends SchedulableEntity {
    protected Task() { super(); }
    public Task(String title, LocalDate dueDate, String description, Status status) {
        super(title, dueDate, description, status);
    }
}
