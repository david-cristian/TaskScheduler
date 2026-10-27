package org.example.utils.Observer;

import org.example.utils.Events.Event;

public interface IObserver<E extends Event> {
    void update(E event);
}
