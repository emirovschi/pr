package com.emirovschi.pr.socket.server;

import com.emirovschi.pr.socket.common.CommandFactory;
import com.emirovschi.pr.socket.common.CommandHandler;
import com.emirovschi.pr.socket.common.DelimiterCommandHandler;
import com.emirovschi.pr.socket.common.ReflectionCommandFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

public class ServerApplication
{
    public static void main(String[] args)
    {
        final ExecutorService executorService = new CustomThreadPoolExecutor(ChatServer.MAX_CONNECTIONS);
        final CommandFactory commandFactory = new ReflectionCommandFactory();
        final CommandHandler commandHandler = new DelimiterCommandHandler(commandFactory);

        final Chat chat = new DynamicChat();
        final Function<Socket, ServerHandler> chatHandlerFactory = new ChatServerHandlerFactory(chat, commandHandler);

        try (final ChatServer chatServer = new ChatServer(27015, executorService, chatHandlerFactory, commandHandler))
        {
            chatServer.run();
        }
        catch (IOException e)
        {
            System.out.println("Server error:" + e.getMessage());
        }
    }
}
