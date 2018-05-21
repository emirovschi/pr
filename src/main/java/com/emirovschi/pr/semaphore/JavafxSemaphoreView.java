package com.emirovschi.pr.semaphore;

import com.emirovschi.pr.semaphore.data.Person;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static javafx.scene.layout.VBox.setVgrow;

public class JavafxSemaphoreView extends HBox implements SemaphoreView
{
    private final TextField nameInput;
    private final TextField timeInput;
    private final Button addInQueue;
    private final ListView<Person> queue;
    private final ListView<Person> club;
    private final StringConverter<BigDecimal> numberConverter;

    public JavafxSemaphoreView()
    {
        final PersonCellFactory personCellFactory = new PersonCellFactory();
        numberConverter = new BigDecimalStringConverter();

        nameInput = new TextField();
        nameInput.setPromptText("Person name");

        timeInput = new TextField();
        timeInput.setPromptText("Time");
        timeInput.setTextFormatter(new TextFormatter<>(numberConverter, BigDecimal.ZERO, this::filter));

        addInQueue = new Button("Add to queue");
        addInQueue.setDisable(true);

        nameInput.textProperty()
                .addListener(this::updateButton);

        timeInput.textProperty()
                .addListener(this::updateButton);

        final VBox addBox = new VBox();
        addBox.setSpacing(20);
        addBox.getChildren().add(nameInput);
        addBox.getChildren().add(timeInput);
        addBox.getChildren().add(addInQueue);

        queue = new ListView<>();
        queue.setCellFactory(personCellFactory);

        final VBox queueBox = new VBox();
        queueBox.setSpacing(20);
        queueBox.getChildren().add(new Label("Queue"));
        queueBox.getChildren().add(queue);
        setVgrow(queue, Priority.ALWAYS);

        club = new ListView<>();
        club.setCellFactory(personCellFactory);

        final VBox clubBox = new VBox();
        clubBox.setSpacing(20);
        clubBox.getChildren().add(new Label("Club"));
        clubBox.getChildren().add(club);
        setVgrow(club, Priority.ALWAYS);

        setPadding(new Insets(20));
        setSpacing(20);
        getChildren().add(addBox);
        getChildren().add(queueBox);
        getChildren().add(clubBox);

        setHgrow(addBox, Priority.ALWAYS);
        setHgrow(queueBox, Priority.ALWAYS);
        setHgrow(clubBox, Priority.ALWAYS);
    }

    private TextFormatter.Change filter(final TextFormatter.Change change)
    {
        final String newText = change.getControlNewText();

        if (newText.isEmpty())
        {
            return change;
        }

        try
        {
            return change;
        }
        catch (final NumberFormatException exception)
        {
            return null;
        }
    }

    private void updateButton(ObservableValue<? extends String> observable, String oldValue, String newValue)
    {
        addInQueue.setDisable(nameInput.getText().length() == 0
                || timeInput.getText().length() == 0
                || numberConverter.fromString(timeInput.getText()).compareTo(BigDecimal.ZERO) <= 0
                || numberConverter.fromString(timeInput.getText()).compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) > 0
                || timeInput.getText().length() == 0);
    }

    @Override
    public void onAdd(final Consumer<Person> onAdd)
    {
        nameInput.setOnAction(event -> action(onAdd));
        addInQueue.setOnAction(event -> action(onAdd));
    }

    private void action(final Consumer<Person> onSubmit)
    {
        final String name = nameInput.getText();
        final String time = timeInput.getText();

        if (name.length() > 0 && time.length() > 0)
        {
            final BigDecimal timeValue = numberConverter.fromString(time);

            if (timeValue.compareTo(BigDecimal.ZERO) > 0 && timeValue.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) <= 0)
            {
                timeInput.setText("");
                nameInput.setText("");
                addInQueue.setDisable(true);
                onSubmit.accept(new Person(name, timeValue.longValue()));
            }
        }
    }

    @Override
    public void addToQueue(final Person person)
    {
        queue.getItems().add(person);
    }

    @Override
    public void moveToClub(final Person person)
    {
        queue.getItems().remove(person);
        club.getItems().add(person);
    }

    @Override
    public void update(final Person person)
    {
        club.refresh();
    }

    @Override
    public void remove(final Person person)
    {
        club.getItems().remove(person);
    }
}
