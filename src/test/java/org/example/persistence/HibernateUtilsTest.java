package org.example.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

public class HibernateUtilsTest {

    @BeforeEach
    void resetSessionFactory() throws Exception {
        Field field = HibernateUtils.class.getDeclaredField("sessionFactory");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    public void testGetSessionFactoryIsCreatedAndOpen() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

        assertNotNull(sessionFactory, "SessionFactory nu ar trebui să fie null");
        assertTrue(sessionFactory.isOpen(), "SessionFactory ar trebui să fie deschisă și activă");
    }

    @Test
    public void testGetSessionFactoryIsSingleton() {
        SessionFactory factory1 = HibernateUtils.getSessionFactory();
        SessionFactory factory2 = HibernateUtils.getSessionFactory();

        assertSame(factory1, factory2, "HibernateUtils nu respectă modelul Singleton; a creat instanțe multiple!");
    }

    @Test
    public void testGetSessionFactoryThrowsExceptionInInitializerErrorOnFailure() {
        try (MockedConstruction<Configuration> mocked = mockConstruction(Configuration.class,
                (mock, context) -> {
                    when(mock.configure(anyString())).thenThrow(new RuntimeException("Simulated configuration failure"));
                })) {

            assertThrows(ExceptionInInitializerError.class, () -> {
                HibernateUtils.getSessionFactory();
            }, "Ar trebui să arunce ExceptionInInitializerError când configurarea eșuează");
        }
    }
}