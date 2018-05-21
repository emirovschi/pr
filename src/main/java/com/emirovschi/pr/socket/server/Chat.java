package com.emirovschi.pr.socket.server;

import com.emirovschi.pr.socket.common.data.Message;

import java.util.function.Consumer;

public interface Chat
{
    void register(final String name, final Consumer<Message> onMessage) throws IllegalArgumentException;

    void unregister(final String name) throws IllegalArgumentException;

    void add(final Message message) throws IllegalArgumentException;
}
