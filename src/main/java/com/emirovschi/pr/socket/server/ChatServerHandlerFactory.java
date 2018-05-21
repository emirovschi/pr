package com.emirovschi.pr.socket.server;

import com.emirovschi.pr.socket.common.CommandHandler;

import java.net.Socket;
import java.util.function.Function;

public class ChatServerHandlerFactory implements Function<Socket, ServerHandler>
{
    private final Chat chat;
    private final CommandHandler commandHandler;

    public ChatServerHandlerFactory(final Chat chat, final CommandHandler commandHandler)
    {
        this.chat = chat;
        this.commandHandler = commandHandler;
    }

    @Override
    public ServerHandler apply(final Socket socket)
    {
        return new ChatServerHandler(chat, commandHandler, socket);
    }
}
