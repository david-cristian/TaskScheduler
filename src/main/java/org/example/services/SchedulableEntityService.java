package org.example.services;

import org.example.model.SchedulableEntity;
import org.example.model.Status;
import org.example.model.Task;
import org.example.persistence.IRepository;
import org.example.persistence.RepositoryObjective;
import org.example.persistence.RepositoryTask;
import org.example.services.Factory.ItemType;
import org.example.services.Factory.SchedulableItemFactory;
import org.example.utils.Events.EntityChangeEvent;
import org.example.utils.Events.EntityChangeEventType;
import org.example.utils.Observer.IObservable;
import org.example.utils.Observer.IObserver;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SchedulableEntityService implements IObservable<EntityChangeEvent> {
    private RepositoryTask repositoryTask;
    private RepositoryObjective repositoryObjective;
    private List<IObserver<EntityChangeEvent>> observers = new ArrayList<>();

    public SchedulableEntityService(RepositoryTask repositoryTask, RepositoryObjective repositoryObjective) {
        this.repositoryTask = repositoryTask;
        this.repositoryObjective = repositoryObjective;
    }

    @Override
    public void addObserver(IObserver<EntityChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver<EntityChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(EntityChangeEvent event) {
        for (IObserver<EntityChangeEvent> observer : observers) {
            observer.update(event);
        }
    }

    @SuppressWarnings("unchecked")
    private <E extends SchedulableEntity>IRepository<Long, E> getRepository(ItemType type) {
        return switch (type) {
            case TASK -> (IRepository<Long, E>) repositoryTask;
            case OBJECTIVE -> (IRepository<Long, E>) repositoryObjective;
        };
    }

    public SchedulableEntity add(ItemType itemType, String title, LocalDate dueDate, String description, String status) {
        SchedulableEntity entity = SchedulableItemFactory.createItem(itemType, title, dueDate, description, Status.valueOf(status));
        SchedulableEntity saved = getRepository(itemType).save(entity);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.ADD, saved));
        return saved;
    }

    public SchedulableEntity update(SchedulableEntity schedulableEntity) {
        ItemType type = (schedulableEntity instanceof Task) ? ItemType.TASK : ItemType.OBJECTIVE;
        SchedulableEntity updated = getRepository(type).save(schedulableEntity);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.UPDATE, updated));
        return updated;
    }

    public void remove(ItemType type, Long id) {
        Optional<? extends SchedulableEntity> entityOpt = getRepository(type).findById(id);
        if (entityOpt.isPresent()) {
            getRepository(type).delete(id);
            notifyObservers(new EntityChangeEvent(EntityChangeEventType.DELETE, entityOpt.get()));
        }
    }

    public List<? extends SchedulableEntity> getAll(ItemType type) {
        return getRepository(type).findAll();
    }

    public List<? extends SchedulableEntity> getAllByInterval(ItemType type, LocalDate from, LocalDate to) {
        return getRepository(type).findByTimeInterval(from, to);
    }

    public List<? extends SchedulableEntity> getAllByDate(ItemType type, LocalDate dueDate) {
        return getRepository(type).findByDate(dueDate);
    }

    public List<? extends SchedulableEntity> getAllByName(ItemType type, String name) {
        return getRepository(type).findByName(name);
    }
}
