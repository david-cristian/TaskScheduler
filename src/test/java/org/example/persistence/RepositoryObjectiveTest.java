package org.example.persistence;

import org.example.model.Objective;
import org.example.model.Status;
import org.example.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepositoryObjectiveTest {
    private RepositoryObjective repoObjective;
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        sessionFactory = HibernateTestUtils.getTestSessionFactory();
        repoObjective = new RepositoryObjective(sessionFactory);
    }

    @Test
    public void test() {
        //testSave
        repoObjective.save(new Objective("Laborator", LocalDate.of(2026, 12, 2), "Rezolva lab fara ai", Status.IN_PROGRESS));
        repoObjective.save(new Objective("Laborator", LocalDate.of(2026, 12, 2), "Rezolva lab fara ai", Status.IN_PROGRESS));
        repoObjective.save(new Objective("Laborator1", LocalDate.of(2026, 12, 3), "Rezolva lab fara ai", Status.IN_PROGRESS));
        repoObjective.save(new Objective("Laborator2", LocalDate.of(2026, 12, 4), "Rezolva lab fara ai", Status.IN_PROGRESS));
        Objective ObjectiveToFind = new Objective("Laborator3", LocalDate.of(2026, 12, 5), "Rezolva lab fara ai", Status.IN_PROGRESS);
        repoObjective.save(ObjectiveToFind);

        //testFindById
        Objective ObjectiveFound = repoObjective.findById(ObjectiveToFind.getId()).orElse(null);
        assertEquals(ObjectiveToFind.getId(), ObjectiveFound.getId());

        //testUpdate
        Objective ObjectiveToFindUpdated = new Objective("Laborator3", LocalDate.of(2026, 12, 5), "Rezolva lab fara ai-uri", Status.IN_PROGRESS);
        ObjectiveToFindUpdated.setId(ObjectiveToFind.getId());
        repoObjective.update(ObjectiveToFind);
        assertEquals("Rezolva lab fara ai-uri", ObjectiveToFindUpdated.getDescription());

        //testFindAll
        assertEquals(5, repoObjective.findAll().size());

        //testFindByDate
        List<Objective> ObjectivesByDate = repoObjective.findByDate(LocalDate.of(2026, 12, 2));
        assertEquals(2, ObjectivesByDate.size());

        //testFindByName
        List<Objective> ObjectivesByName = repoObjective.findByName("Laborator");
        assertEquals(2, ObjectivesByName.size());

        //testFindByInterval
        List<Objective> ObjectivesByInterval = repoObjective.findByTimeInterval(LocalDate.of(2026, 12, 2), LocalDate.of(2026, 12, 5));
        assertEquals(5, ObjectivesByInterval.size());

        //testDelete
        repoObjective.delete(ObjectiveToFind.getId());
        List<Objective> ObjectivesAfterDelete = repoObjective.findAll();

        assertTrue(ObjectivesAfterDelete.stream().noneMatch(Objective -> Objective.getId().equals(ObjectiveToFind.getId())));
        try {
            repoObjective.delete(ObjectiveToFind.getId());
        } catch (RepositoryException ex) {
            assertEquals("Hibernate error: Entity with id " + ObjectiveToFind.getId() + " not found", ex.getMessage());
        }
    }

    @SuppressWarnings("JpaQlInspection")
    @AfterEach
    public void tearDown() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Objective").executeUpdate();
            tx.commit();
        }
    }
}
