package com.emirovschi.pr.socket.server;

import com.emirovschi.pr.socket.common.data.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class DynamicChat implements Chat
{
    private static final String SYSTEM = "System";

    private Map<String, Consumer<Message>> users;

    public DynamicChat()
    {
        users = new ConcurrentHashMap<>();
        users.put(SYSTEM, message-> { /*ignore messages for system */ });
    }

    @Override
    public void register(final String name, final Consumer<Message> onMessage) throws IllegalArgumentException
    {
        if (users.containsKey(name))
        {
            System.out.println("Failed to login with name [" + name + "] as it already exists");
            throw new IllegalArgumentException("Name [" + name + "] already exists");
        }

        add(new Message(SYSTEM, name + " joined the chat."));
        users.put(name, onMessage);
    }

    @Override
    public void unregister(final String name) throws IllegalArgumentException
    {
        if (!users.containsKey(name))
        {
            throw new IllegalArgumentException("Name [" + name + "] doesn't exist");
        }

        users.remove(name);
        add(new Message(SYSTEM, name + " left the chat."));
    }

    @Override
    public void add(final Message message) throws IllegalArgumentException
    {
        if (!users.containsKey(message.getName()))
        {
            throw new IllegalArgumentException("Name [" + message.getName() + "] doesn't exist");
        }

        users.entrySet().stream()
            .filter(entry -> !entry.getKey().equals(message.getName()))
            .map(Map.Entry::getValue)
            .forEach(consumer -> consumer.accept(message));
    }
}
