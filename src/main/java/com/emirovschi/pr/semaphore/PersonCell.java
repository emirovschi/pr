package com.emirovschi.pr.semaphore;

import com.emirovschi.pr.semaphore.data.Person;
import javafx.scene.control.ListCell;

public class PersonCell extends ListCell<Person>
{
    @Override
    protected void updateItem(final Person item, final boolean empty)
    {
        super.updateItem(item, empty);

        if (empty || item == null)
        {
            setText(null);
        }
        else
        {
            setText(item.getName() + " - " + item.getTime());
        }
    }
}
