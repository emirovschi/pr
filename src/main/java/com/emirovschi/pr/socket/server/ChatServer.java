package com.emirovschi.pr.socket.server;

import com.emirovschi.pr.socket.common.CommandHandler;
import com.emirovschi.pr.socket.common.data.ConnectResponseCommand;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;

public class ChatServer implements Server
{
    public static final int MAX_CONNECTIONS = 10;

    private final int port;
    private final ExecutorService executorService;
    private final Function<Socket, ServerHandler> serverHandlerFactory;
    private final CommandHandler commandHandler;

    private ServerSocket serverSocket;

    public ChatServer(
            final int port,
            final ExecutorService executorService,
            final Function<Socket, ServerHandler> serverHandlerFactory, final CommandHandler commandHandler)
    {
        this.port = port;
        this.executorService = executorService;
        this.serverHandlerFactory = serverHandlerFactory;
        this.commandHandler = commandHandler;
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
            System.out.println("An error occurred while starting the client: " + exception.getMessage());
        }
    }

    private void internalRun() throws IOException
    {
        serverSocket = new ServerSocket(port, MAX_CONNECTIONS);
        System.out.println("Server listening on port " + port);

        while (!serverSocket.isClosed())
        {
            accept();
        }
    }

    private void accept()
    {
        try
        {
            final Socket socket = serverSocket.accept();
            System.out.println("Client connected " + socket.toString());

            try
            {
                executorService.execute(() -> handleClient(socket));
            }
            catch (final RejectedExecutionException e)
            {
                System.out.println("Server full, rejecting:" + socket);
                commandHandler.write(socket.getOutputStream(), new ConnectResponseCommand(false));
            }
        }
        catch (final IOException e)
        {
            System.out.println("Client connection failed:" + e.getMessage());
        }
    }

    private void handleClient(final Socket socket)
    {
        try (final ServerHandler serverHandler = serverHandlerFactory.apply(socket))
        {
            serverHandler.run();
        }
        catch (final IOException e)
        {
            System.out.println("An error occurred during while handling client [" + socket +"]: " + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException
    {
        if (serverSocket == null)
        {
            throw new IOException("Failed to stop server as it was not started");
        }

        serverSocket.close();
    }
}
