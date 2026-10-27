package org.example.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRepository<ID, T> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findByDate(LocalDate date);
    List<T> findByName(String name);
    List<T> findByTimeInterval(LocalDate from, LocalDate to);
    List<T> findAll();
    T update(T entity);
    void delete(ID id);
}
