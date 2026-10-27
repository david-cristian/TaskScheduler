package org.example.persistence;

import org.example.model.Objective;
import org.hibernate.SessionFactory;

public class RepositoryObjective extends AbstractRepository<Long, Objective> implements IRepositoryObjective {
    public RepositoryObjective(SessionFactory sessionFactory) {
        super(sessionFactory, Objective.class);
    }

}
