package com.emirovschi.pr.socket.common.data;

import java.util.Map;

public class ReceiveMessageCommand extends AbstractCommand
{
    private static final String NAME = "name";
    private static final String TEXT = "text";

    public ReceiveMessageCommand(final Message message)
    {
        super(NAME, message.getName(), TEXT, message.getText());
    }

    public ReceiveMessageCommand(final Map<String, String> data)
    {
        super(data);
    }

    public String getName()
    {
        return getData().get(NAME);
    }

    public void setName(final String name)
    {
        getData().put(NAME, name);
    }

    public String getText()
    {
        return getData().get(TEXT);
    }

    public void setText(final String text)
    {
        getData().put(TEXT, text);
    }

    public Message getMessage()
    {
        return new Message(getName(), getText());
    }
}
