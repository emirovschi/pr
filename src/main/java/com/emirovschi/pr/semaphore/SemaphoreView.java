package com.emirovschi.pr.semaphore;

import com.emirovschi.pr.semaphore.data.Person;

import java.util.function.Consumer;

public interface SemaphoreView
{
    void onAdd(Consumer<Person> onAdd);

    void addToQueue(Person person);

    void moveToClub(Person person);

    void update(Person person);

    void remove(Person person);
}
