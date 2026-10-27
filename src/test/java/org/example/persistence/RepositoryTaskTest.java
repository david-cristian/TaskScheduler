package org.example.persistence;

import org.example.model.Status;
import org.example.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepositoryTaskTest {
    private RepositoryTask repoTask;
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        sessionFactory = HibernateTestUtils.getTestSessionFactory();
        repoTask = new RepositoryTask(sessionFactory);
    }

    @Test
    public void test() {
        //testSave
        repoTask.save(new Task("Laborator", LocalDate.of(2026, 12, 2), "Rezolva lab fara ai", Status.IN_PROGRESS));
        repoTask.save(new Task("Laborator", LocalDate.of(2026, 12, 2), "Rezolva lab fara ai", Status.IN_PROGRESS));
        repoTask.save(new Task("Laborator1", LocalDate.of(2026, 12, 3), "Rezolva lab fara ai", Status.IN_PROGRESS));
        repoTask.save(new Task("Laborator2", LocalDate.of(2026, 12, 4), "Rezolva lab fara ai", Status.IN_PROGRESS));
        Task taskToFind = new Task("Laborator3", LocalDate.of(2026, 12, 5), "Rezolva lab fara ai", Status.IN_PROGRESS);
        repoTask.save(taskToFind);

        //testFindById
        Task taskFound = repoTask.findById(taskToFind.getId()).orElse(null);
        assertEquals(taskToFind.getId(), taskFound.getId());

        //testUpdate
        Task taskToFindUpdated = new Task("Laborator3", LocalDate.of(2026, 12, 5), "Rezolva lab fara ai-uri", Status.IN_PROGRESS);
        taskToFindUpdated.setId(taskToFind.getId());
        repoTask.update(taskToFind);
        assertEquals("Rezolva lab fara ai-uri", taskToFindUpdated.getDescription());

        //testFindAll
        assertEquals(5, repoTask.findAll().size());

        //testFindByDate
        List<Task> tasksByDate = repoTask.findByDate(LocalDate.of(2026, 12, 2));
        assertEquals(2, tasksByDate.size());

        //testFindByName
        List<Task> tasksByName = repoTask.findByName("Laborator");
        assertEquals(2, tasksByName.size());

        //testFindByInterval
        List<Task> tasksByInterval = repoTask.findByTimeInterval(LocalDate.of(2026, 12, 2), LocalDate.of(2026, 12, 5));
        assertEquals(5, tasksByInterval.size());

        //testDelete
        repoTask.delete(taskToFind.getId());
        List<Task> tasksAfterDelete = repoTask.findAll();

        assertTrue(tasksAfterDelete.stream().noneMatch(task -> task.getId().equals(taskToFind.getId())));
        try {
            repoTask.delete(taskToFind.getId());
        } catch (RepositoryException ex) {
            assertEquals("Hibernate error: Entity with id " + taskToFind.getId() + " not found", ex.getMessage());
        }
    }

    @SuppressWarnings("JpaQlInspection")
    @AfterEach
    public void tearDown() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Task").executeUpdate();
            tx.commit();
        }
    }
}
