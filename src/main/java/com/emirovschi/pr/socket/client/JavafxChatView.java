package com.emirovschi.pr.socket.client;

import com.emirovschi.pr.socket.common.data.Message;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

import static javafx.scene.layout.HBox.setHgrow;

public class JavafxChatView extends VBox implements ChatView
{
    public static final String NEW_LINE = System.getProperty("line.separator");

    private final TextArea output;
    private final TextField input;
    private final Button submitButton;
    private String name;
    private final StringBuilder outputText;

    public JavafxChatView()
    {
        output = new TextArea();
        output.setPromptText("No messages");
        output.setEditable(false);
        output.setFocusTraversable(false);

        input = new TextField();
        input.setPromptText("Write message");

        submitButton = new Button();
        submitButton.setText("Send");
        submitButton.setDisable(true);

        input.textProperty()
                .addListener((observable, oldValue, newValue) -> submitButton.setDisable(newValue.length() == 0));

        final HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.getChildren().add(input);
        hbox.getChildren().add(submitButton);
        setHgrow(input, Priority.ALWAYS);

        setPadding(new Insets(20));
        setSpacing(20);
        getChildren().add(output);
        getChildren().add(hbox);
        setVgrow(output, Priority.ALWAYS);

        outputText = new StringBuilder();
    }

    @Override
    public void setName(final String name)
    {
        this.name = name;
    }

    @Override
    public void addMessage(final Message message)
    {
        outputText.append(message.getName());
        outputText.append(": ");
        outputText.append(message.getText());
        outputText.append(NEW_LINE);
        output.setText(outputText.toString());
    }

    @Override
    public void onSend(final Consumer<String> onSubmit)
    {
        input.setOnAction(event -> action(onSubmit));
        submitButton.setOnAction(event -> action(onSubmit));
    }

    private void action(final Consumer<String> onSubmit)
    {
        final String message = input.getText();

        if (message.length() > 0)
        {
            input.setText("");
            input.setDisable(true);
            onSubmit.accept(message);
            input.setDisable(false);
            addMessage(new Message(name, message));
        }
    }
}
