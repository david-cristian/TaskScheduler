package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "objectives")
public class Objective extends SchedulableEntity {
    protected Objective() {
        super();
    }

    public Objective(String title, LocalDate dueDate, String description, Status status) {
        super(title, dueDate, description, status);
    }
}
