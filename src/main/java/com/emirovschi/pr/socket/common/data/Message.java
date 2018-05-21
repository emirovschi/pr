package com.emirovschi.pr.socket.common.data;

public class Message
{
    private final String name;
    private final String text;

    public Message(final String name, final String text)
    {
        this.name = name;
        this.text = text;
    }

    public String getName()
    {
        return name;
    }

    public String getText()
    {
        return text;
    }

    @Override
    public String toString()
    {
        return "Message{" +
            "name='" + name + '\'' +
            ", text='" + text + '\'' +
            '}';
    }
}
