package org.example.services.Factory;

import org.example.model.Objective;
import org.example.model.SchedulableEntity;
import org.example.model.Status;
import org.example.model.Task;

import java.time.LocalDate;

public class SchedulableItemFactory {
    public static SchedulableEntity createItem(ItemType type, String title, LocalDate dueDate, String description, Status status) {
        return switch (type) {
            case TASK -> new Task(title, dueDate, description, status);
            case OBJECTIVE -> new Objective(title, dueDate, description, status);
        };
    }
}
