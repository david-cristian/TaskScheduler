package org.example.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Entity;
import org.example.model.SchedulableEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRepository<ID extends Number, T extends SchedulableEntity> implements IRepository<ID, T> {
    protected final SessionFactory sessionFactory;
    protected final Class<T> entityClass;
    protected static final Logger logger = LogManager.getLogger();

    public AbstractRepository(SessionFactory sessionFactory, Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
        logger.info("Initializing AbstractRepository");
    }

    @Override
    public T save(T entity) {
        logger.traceEntry("saving entity {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(entity);
                tx.commit();
                return entity;
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Hibernate error: " + e.getMessage());
                throw new RepositoryException("Hibernate error: " + e.getMessage());
            }
        }
    }

    @Override
    public  T update(T entity) {
        logger.traceEntry("updating entity {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                T updatedEntity = session.merge(entity);
                tx.commit();
                return updatedEntity;
            }  catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                logger.error("Hibernate error: " + e.getMessage());
                throw new RepositoryException("Hibernate error: " + e.getMessage());
            }
        }
    }

    @Override
    public void delete(ID id) {
        logger.traceEntry("deleting entity with id {}", id);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                T entity = session.get(entityClass, id);
                if (entity != null) {
                    session.remove(entity);
                    tx.commit();
                    logger.traceExit("deleted entity with id {}", id);
                } else {
                    logger.warn("entity with id {} not found", id);
                    tx.rollback();
                    throw new RepositoryException("Entity with id " + id + " not found");
                }
            } catch (RuntimeException e) {
                if (tx != null && tx.isActive()) {
                    try {
                        tx.rollback();
                    } catch (Exception ex) {
                        logger.traceExit("Failed to rollback: ", ex);
                    }
                }
                logger.error("Hibernate error: " + e.getMessage());
                throw new RepositoryException("Hibernate error: " + e.getMessage());
            }
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        logger.traceEntry("finding entity with id {}", id);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                T entity = session.get(entityClass, id);
                tx.commit();
                return Optional.ofNullable(entity);
            } catch (RuntimeException e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                logger.error("Hibernate error: " + e.getMessage());
                return Optional.empty();
            }
        }
    }

    @Override
    public List<T> findAll() {
        logger.traceEntry("finding all entities");
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<T> allEntities = session.createQuery("from " + entityClass.getName(), entityClass).getResultList();
                tx.commit();
                logger.traceExit("found {} entities", allEntities.size());
                return allEntities;
            } catch (RuntimeException e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                logger.error("Hibernate error: " + e.getMessage());
                return List.of();
            }
        }
    }

    @SuppressWarnings("JpaQlInspection")
    @Override
    public List<T> findByDate(LocalDate date) {
        logger.traceEntry("finding all entities by date {}", date);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<T> allEntities = session.createQuery("from " + entityClass.getName() + " where dueDate = :dueDate", entityClass).setParameter("dueDate", date).getResultList();
                tx.commit();
                logger.traceExit("found {} entities", allEntities.size());
                return allEntities;
            }  catch (RuntimeException e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                logger.error("Hibernate error: " + e.getMessage());
                return List.of();
            }
        }
    }

    @SuppressWarnings("JpaQlInspection")
    @Override
    public List<T> findByName(String title) {
        logger.traceEntry("finding all entities by name {}", title);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<T> allEntities = session.createQuery("from " + entityClass.getName() + " where title = :title", entityClass).setParameter("title", title).getResultList();
                tx.commit();
                logger.traceExit("found {} entities", allEntities.size());
                return allEntities;
            }   catch (RuntimeException e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                logger.error("Hibernate error: " + e.getMessage());
                return List.of();
            }
        }
    }

    @SuppressWarnings("JpaQlInspection")
    @Override
    public List<T> findByTimeInterval(LocalDate from, LocalDate to) {
        logger.traceEntry("finding all entities by date between {} and {}", from, to);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<T> allEntities = session.createQuery("from " + entityClass.getName() + " where dueDate between :from and :to", entityClass).setParameter("from", from).setParameter("to", to).getResultList();
                tx.commit();
                logger.traceExit("found {} entities", allEntities.size());
                return allEntities;
            } catch (RuntimeException e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                logger.error("Hibernate error: " + e.getMessage());
                return List.of();
            }
        }
    }
}
