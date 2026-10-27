package org.example.persistence;

import org.example.model.Task;
import org.hibernate.SessionFactory;

public class RepositoryTask extends AbstractRepository<Long, Task> implements IRepositoryTask {
    public RepositoryTask(SessionFactory sessionFactory) {
        super(sessionFactory, Task.class);
    }
}
