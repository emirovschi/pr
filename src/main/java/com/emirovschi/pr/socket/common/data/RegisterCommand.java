package com.emirovschi.pr.socket.common.data;

import java.util.Map;

public class RegisterCommand extends AbstractCommand
{
    private static final String NAME = "name";

    public RegisterCommand(final String name)
    {
        super(NAME, name);
    }

    public RegisterCommand(final Map<String, String> data)
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
}
