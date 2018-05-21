package com.emirovschi.pr.socket.client;

import com.emirovschi.pr.socket.common.data.Message;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

public interface Client extends Runnable, Closeable
{
    void register(String name) throws IOException;

    void send(String message);

    void onMessage(Consumer<Message> onMessage);

    void onError(Consumer<String> onError);
}
