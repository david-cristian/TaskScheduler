package org.example.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskTest {
    @Test
    public void test() {
        Task t1 = new Task("Laborator", LocalDate.of(2026, 12, 2), "Rezolva lab fara ai", Status.IN_PROGRESS);
        assertNull(t1.getId(), "ID-ul trebuie să fie null înainte de salvarea în DB");
        assertEquals("Laborator", t1.getTitle());
        assertEquals(LocalDate.of(2026, 12, 2), t1.getDueDate());
        assertEquals("Rezolva lab fara ai", t1.getDescription());
        assertEquals(Status.IN_PROGRESS, t1.getStatus());

        Task t2 = new Task();
        t2.setId(t1.getId());
        t2.setTitle(t1.getTitle());
        t2.setDueDate(t1.getDueDate());
        t2.setDescription(t1.getDescription());
        t2.setStatus(t1.getStatus());

        assertNull(t2.getId(), "ID-ul trebuie să fie null înainte de salvarea în DB");
        assertEquals("Laborator", t2.getTitle());
        assertEquals(t1.getDueDate(), t2.getDueDate());
        assertEquals(t1.getDescription(), t2.getDescription());
        assertEquals(t1.getStatus(), t2.getStatus());
    }
}
