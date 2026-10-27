package org.example.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ObjectiveTest {
    @Test
    public void test() {
        Objective obj = new Objective("100k", LocalDate.of(2026, 1, 1), "bani");
        assertNull(obj.getId(), "ID-ul trebuie să fie null înainte de salvarea în DB");
        assertEquals("100k", obj.getTitle());
        assertEquals(LocalDate.of(2026, 1, 1), obj.getDueDate());
        assertEquals("bani", obj.getDescription());

        Objective obj1 = new Objective();
        obj1.setId(obj.getId());
        obj1.setTitle(obj.getTitle());
        obj1.setDueDate(obj.getDueDate());
        obj1.setDescription(obj.getDescription());

        assertNull(obj1.getId(), "ID-ul trebuie să fie null înainte de salvarea în DB");
        assertEquals(obj.getTitle(), obj1.getTitle());
        assertEquals(obj.getDueDate(), obj1.getDueDate());
        assertEquals(obj.getDescription(), obj1.getDescription());
    }
}
