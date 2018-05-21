package com.emirovschi.pr.socket.client;

import com.emirovschi.pr.socket.common.CommandHandler;
import com.emirovschi.pr.socket.common.data.ConnectResponseCommand;
import com.emirovschi.pr.socket.common.data.LoginResponseCommand;
import com.emirovschi.pr.socket.common.data.Message;
import com.emirovschi.pr.socket.common.data.ReceiveMessageCommand;
import com.emirovschi.pr.socket.common.data.RegisterCommand;
import com.emirovschi.pr.socket.common.data.SendMessageCommand;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class ChatClient implements Client
{
    private final String serverIp;
    private final int serverPort;
    private final CommandHandler commandHandler;
    private final ExecutorService executorService;

    private Socket socket;
    private Consumer<Message> onMessage;
    private Consumer<String> onError;

    public ChatClient(
        final String serverIp,
        final int serverPort,
        final CommandHandler commandHandler,
        final ExecutorService executorService)
    {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.commandHandler = commandHandler;
        this.executorService = executorService;
    }

    @Override
    public void register(final String name) throws IllegalArgumentException
    {
        try
        {
            commandHandler.write(socket.getOutputStream(), new RegisterCommand(name));
            final LoginResponseCommand response = commandHandler.read(socket.getInputStream());

            if (response.getSuccess())
            {
                executorService.execute(this::listen);
            }
            else
            {
                throw new IllegalArgumentException();
            }
        }
        catch (final IOException exception)
        {
            System.out.println("Failed to register: " + exception.getMessage());
            onError.accept(exception.getMessage());
        }
    }

    private void listen()
    {
        try
        {
            while (socket.isConnected())
            {
                final ReceiveMessageCommand command = commandHandler.read(socket.getInputStream());
                onMessage.accept(command.getMessage());
            }
        }
        catch (final IOException exception)
        {
            System.out.println("Failed to read message:" + exception.getMessage());
            Platform.runLater(() -> onError.accept(exception.getMessage()));
        }
    }

    @Override
    public void send(final String message)
    {
        try
        {
            commandHandler.write(socket.getOutputStream(), new SendMessageCommand(message));
        }
        catch (final IOException exception)
        {
            System.out.println("Failed to send message: " + exception.getMessage());
            onError.accept(exception.getMessage());
        }
    }

    @Override
    public void onMessage(final Consumer<Message> onMessage)
    {
        this.onMessage = onMessage;
    }

    @Override
    public void onError(final Consumer<String> onError)
    {
        this.onError = onError;
    }

    @Override
    public void run()
    {
        final ConnectResponseCommand command;

        try
        {
            socket = new Socket(serverIp, serverPort);
            command = commandHandler.read(socket.getInputStream());
        }
        catch (final Exception exception)
        {
            System.out.println("An error occurred with client [" + socket + "]: " + exception.getMessage());
            throw new RuntimeException("Could not connect to the server");
        }

        if (!command.getSuccess())
        {
            throw new RuntimeException("The server is full");
        }
    }

    @Override
    public void close() throws IOException
    {
        socket.close();
    }
}
