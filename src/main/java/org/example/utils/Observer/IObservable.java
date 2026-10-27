package org.example.utils.Observer;

import org.example.utils.Events.Event;

public interface IObservable<E extends Event> {
    void addObserver(IObserver<E> observer);
    void removeObserver(IObserver<E> observer);
    void notifyObservers(E event);
}
