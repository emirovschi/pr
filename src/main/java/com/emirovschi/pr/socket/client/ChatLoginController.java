package com.emirovschi.pr.socket.client;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatLoginController
{
    private final Client client;
    private final Stage primaryStage;
    private final ChatLoginView chatLoginView;
    private final Scene chatScene;

    public ChatLoginController(
            final Client client,
            final Stage primaryStage,
            final ChatLoginView chatLoginView,
            final Scene chatScene)
    {
        this.client = client;
        this.primaryStage = primaryStage;
        this.chatLoginView = chatLoginView;
        this.chatScene = chatScene;
        chatLoginView.onSubmit(this::submit);
    }

    private void submit(final String name)
    {
        try
        {
            client.register(name);
            ((ChatView) chatScene.getRoot()).setName(name);
            primaryStage.setScene(chatScene);
            primaryStage.setResizable(true);
            primaryStage.setMinHeight(400);
            primaryStage.setMinWidth(200);
        }
        catch (IllegalArgumentException e)
        {
            new Alert(Alert.AlertType.WARNING, "Username is already in use").showAndWait();
            chatLoginView.reset();
        }
        catch (IOException e)
        {
            new Alert(Alert.AlertType.ERROR, "Could not register").showAndWait();
        }
    }
}
