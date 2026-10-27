package org.example.persistence;

import org.example.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AbstractRepositoryTest {
    @Mock private SessionFactory mockSessionFactory;
    @Mock private Session mockSession;
    @Mock private Transaction mockTransaction;
    @Mock private Query<Task> mockQuery;

    private AbstractRepository<Long, Task> testRepository;

    @BeforeEach
    public void setUp() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        lenient().when(mockTransaction.isActive()).thenReturn(true);
        testRepository = new AbstractRepository<>(mockSessionFactory, Task.class) {};
    }

    @Test
    public void testSaveShouldRollbackAndThrowException() {
        doThrow(new RuntimeException("Simulated DB error")).when(mockSession).persist(any());
        Task testTask = new Task("Test", LocalDate.now(), "Info");
        assertThrows(RepositoryException.class, () -> testRepository.save(testTask));
        verify(mockTransaction, times(1)).rollback();
    }

    @Test
    public void testUpdateShouldRollbackAndThrowException() {
        when(mockSession.merge(any())).thenThrow(new RuntimeException("Simulated DB error"));
        assertThrows(RepositoryException.class, () -> testRepository.update(new Task("Test", LocalDate.now(), "Info")));
        verify(mockTransaction).rollback();
    }

    @Test
    public void testDeleteShouldRollbackAndThrowException() {
        when(mockSession.get(eq(Task.class), anyLong())).thenThrow(new RuntimeException("Error"));
        assertThrows(RepositoryException.class, () -> testRepository.delete(1L));
        verify(mockTransaction).rollback();
    }

    @Test
    public void testFindByIdShouldReturnEmptyOptional() {
        when(mockSession.get(eq(Task.class), anyLong())).thenThrow(new RuntimeException("Error"));
        Optional<Task> result = testRepository.findById(1L);
        assertTrue(result.isEmpty(),"Trebuie să returneze Optional.empty() pe eroare" );
        verify(mockTransaction).rollback();
    }

    @Test
    public void testEveryFindAllShouldReturnEmptyList() {
        when(mockSession.createQuery(anyString(), eq(Task.class))).thenThrow(new RuntimeException("Error"));
        assertTrue(testRepository.findAll().isEmpty());
        assertTrue(testRepository.findByDate(LocalDate.now()).isEmpty());
        assertTrue(testRepository.findByName("Test").isEmpty());
        assertTrue(testRepository.findByTimeInterval(LocalDate.now(), LocalDate.now()).isEmpty());

        verify(mockTransaction, times(4)).rollback();
    }

}
