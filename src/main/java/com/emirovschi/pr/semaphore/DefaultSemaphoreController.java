package com.emirovschi.pr.semaphore;

import com.emirovschi.pr.semaphore.data.Person;
import javafx.application.Platform;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class DefaultSemaphoreController implements SemaphoreController
{
    private final SemaphoreView semaphoreView;
    private final Semaphore semaphore;
    private final Map<Person, PersonTask> threads;

    public DefaultSemaphoreController(final SemaphoreView semaphoreView, final Semaphore semaphore)
    {
        this.semaphoreView = semaphoreView;
        this.semaphore = semaphore;
        this.threads = new ConcurrentHashMap<>();

        semaphoreView.onAdd(this::add);
    }

    private void add(final Person person)
    {
        final PersonTask personTask = new PersonTask(person, semaphore, this);
        final Thread thread = new Thread(personTask);
        thread.start();
        threads.put(person, personTask);
    }

    @Override
    public void wait(final Person person)
    {
        Platform.runLater(() -> semaphoreView.addToQueue(person));
    }

    @Override
    public void go(final Person person)
    {
        Platform.runLater(() -> semaphoreView.moveToClub(person));
    }

    @Override
    public void update(final Person person)
    {
        Platform.runLater(() -> semaphoreView.update(person));
    }

    @Override
    public void leave(final Person person)
    {
        threads.remove(person);
        Platform.runLater(() -> semaphoreView.remove(person));
    }

    @Override
    public void stop()
    {
        threads.values().forEach(PersonTask::stop);
    }
}
