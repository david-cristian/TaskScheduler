package org.example.persistence;

import org.example.model.Objective;
import org.example.model.Task;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");

                configuration.addAnnotatedClass(Task.class);
                configuration.addAnnotatedClass(Objective.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());

                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.err.println("Eroare la initializarea Hibernate SessionFactory: " + e);
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }
}
