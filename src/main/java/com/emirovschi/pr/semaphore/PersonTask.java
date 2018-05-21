package com.emirovschi.pr.semaphore;

import com.emirovschi.pr.semaphore.data.Person;

import java.util.concurrent.Semaphore;

public class PersonTask implements Runnable
{
    private final Person person;
    private final Semaphore semaphore;
    private final SemaphoreController semaphoreController;
    private boolean run;

    public PersonTask(final Person person, final Semaphore semaphore, final SemaphoreController semaphoreController)
    {
        this.person = person;
        this.semaphore = semaphore;
        this.semaphoreController = semaphoreController;
        this.run = true;
    }

    @Override
    public void run()
    {
        try
        {
            semaphoreController.wait(person);
            semaphore.acquire();
            semaphoreController.go(person);

            while(run && person.getTime() > 0)
            {
                Thread.sleep(1000);
                person.setTime(person.getTime() - 1);
                semaphoreController.update(person);
            }

            semaphoreController.leave(person);
            semaphore.release();
        }
        catch (final InterruptedException exception)
        {
            exception.printStackTrace();
        }
    }

    public void stop()
    {
        this.run = false;
    }
}
