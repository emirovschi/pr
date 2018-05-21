package com.emirovschi.pr.socket.client;

import com.emirovschi.pr.socket.common.CommandFactory;
import com.emirovschi.pr.socket.common.CommandHandler;
import com.emirovschi.pr.socket.common.DelimiterCommandHandler;
import com.emirovschi.pr.socket.common.ReflectionCommandFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApplication extends Application
{
    private static Client CLIENT;
    private static Stage PRIMARY_STAGE;

    private ChatLoginController chatLoginController;
    private ChatController chatController;

    @Override
    public void start(final Stage primaryStage)
    {
        PRIMARY_STAGE = primaryStage;

        final JavafxChatLoginView chatLoginView = new JavafxChatLoginView();
        final Scene chatLoginScene = new Scene(chatLoginView, 300, 100);

        final JavafxChatView chatView = new JavafxChatView();
        final Scene chatScene = new Scene(chatView, 800, 600);

        primaryStage.setTitle("Chat Client");
        primaryStage.setResizable(false);
        primaryStage.setScene(chatLoginScene);

        chatLoginController = new ChatLoginController(CLIENT, primaryStage, chatLoginView, chatScene);
        chatController = new ChatController(CLIENT, chatView);

        try
        {
            CLIENT.run();
            primaryStage.show();
        }
        catch (Exception exception)
        {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            primaryStage.close();
        }
    }

    public static void main(final String[] args)
    {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final CommandFactory commandFactory = new ReflectionCommandFactory();
        final CommandHandler commandHandler = new DelimiterCommandHandler(commandFactory);

        try (final Client client = new ChatClient("localhost", 27015, commandHandler, executorService))
        {
            CLIENT = client;
            CLIENT.onError(error -> {
                new Alert(Alert.AlertType.ERROR, error).showAndWait();
                PRIMARY_STAGE.close();
            });
            CLIENT.onMessage(message -> { /* ignore until initialized */ });

            launch(args);
        }
        catch (Exception e)
        {
            System.out.println("Failed to start application");
        }
        finally
        {
            executorService.shutdown();
        }
        //TODO: system messages (join/left)
    }
}
