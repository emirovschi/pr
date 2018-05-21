package com.emirovschi.pr.socket.common.data;

import java.util.Map;

public class SendMessageCommand extends AbstractCommand
{
    private static final String TEXT = "text";

    public SendMessageCommand(final String text)
    {
        super(TEXT, text);
    }

    public SendMessageCommand(final Map<String, String> data)
    {
        super(data);
    }

    public String getText()
    {
        return getData().get(TEXT);
    }

    public void setText(final String text)
    {
        getData().put(TEXT, text);
    }
}
