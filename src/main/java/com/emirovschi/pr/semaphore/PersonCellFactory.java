package com.emirovschi.pr.semaphore;

import com.emirovschi.pr.semaphore.data.Person;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class PersonCellFactory implements Callback<ListView<Person>, ListCell<Person>>
{
    @Override
    public ListCell<Person> call(final ListView<Person> param)
    {
        return new PersonCell();
    }
}
