package com.emirovschi.pr.semaphore;

import com.emirovschi.pr.semaphore.data.Person;

public interface SemaphoreController
{
    void wait(Person person);

    void go(Person person);

    void update(Person person);

    void leave(Person person);

    void stop();
}
