package org.example.persistence;

import org.example.model.Objective;
import org.example.model.Task;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateTestUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getTestSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate-test.cfg.xml");

                configuration.addAnnotatedClass(Task.class);
                configuration.addAnnotatedClass(Objective.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());

                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }
}