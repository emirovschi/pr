package com.emirovschi.pr.socket.server;

import com.emirovschi.pr.socket.common.CommandHandler;
import com.emirovschi.pr.socket.common.data.ConnectResponseCommand;
import com.emirovschi.pr.socket.common.data.LoginResponseCommand;
import com.emirovschi.pr.socket.common.data.Message;
import com.emirovschi.pr.socket.common.data.ReceiveMessageCommand;
import com.emirovschi.pr.socket.common.data.RegisterCommand;
import com.emirovschi.pr.socket.common.data.SendMessageCommand;

import java.io.IOException;
import java.net.Socket;

public class ChatServerHandler implements ServerHandler
{
    private final Chat chat;
    private final CommandHandler commandHandler;
    private final Socket socket;

    private String name;

    public ChatServerHandler(final Chat chat, final CommandHandler commandHandler, final Socket socket)
    {
        this.chat = chat;
        this.commandHandler = commandHandler;
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            internalRun();
        }
        catch (final Exception exception)
        {
            System.out.println("An error occurred with client [" + socket + "]: " + exception.getMessage());
        }
    }

    private void internalRun() throws IOException
    {
        commandHandler.write(socket.getOutputStream(), new ConnectResponseCommand(true));

        while (name == null)
        {
            final RegisterCommand registerCommand = commandHandler.read(socket.getInputStream());

            try
            {
                chat.register(registerCommand.getName(), this::sendMessage);
                name = registerCommand.getName();
            }
            catch (final IllegalArgumentException exception)
            {
                commandHandler.write(socket.getOutputStream(), new LoginResponseCommand(false));
            }
        }

        System.out.println("Registered client [" + socket + "] with name [" + name + "]");
        commandHandler.write(socket.getOutputStream(), new LoginResponseCommand(true));

        while (socket.isConnected())
        {
            final SendMessageCommand command = commandHandler.read(socket.getInputStream());
            System.out.println("Read message [" + command.getText() + "] from [" + socket + "]");
            chat.add(new Message(name, command.getText()));
        }
    }

    private synchronized void sendMessage(final Message message)
    {
        try
        {
            System.out.println("Sending message [" + message + "] to [" + socket + "]");
            commandHandler.write(socket.getOutputStream(), new ReceiveMessageCommand(message));
        }
        catch (final IOException exception)
        {
            System.out.println("Could not send message to [" + socket + "]: " + exception.getMessage());
            closeQuietly();
        }
    }

    private void closeQuietly()
    {
        try
        {
            close();
        }
        catch (IOException e)
        {
            System.out.println("Could not close server handler:" + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException
    {
        if (name != null)
        {
            System.out.println("Unregistered client [" + socket + "] with name [" + name + "]");
            chat.unregister(name);
        }
        socket.close();
    }
}
