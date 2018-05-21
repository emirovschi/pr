package com.emirovschi.pr.socket.common.data;

import java.util.HashMap;
import java.util.Map;

public class AbstractCommand implements Command
{
    private final Map<String, String> data;

    public AbstractCommand(final String... data)
    {
        if (data.length % 2 != 0)
        {
            throw new IllegalArgumentException("Data should have an even number of values");
        }

        this.data = new HashMap<>();

        for (int i = 0; i < data.length; i+=2)
        {
            this.data.put(data[i], data[i + 1]);
        }
    }

    public AbstractCommand(final Map<String, String> data)
    {
        this.data = data;
    }

    @Override
    public String getCommandType()
    {
        return getClass().getSimpleName();
    }

    @Override
    public Map<String, String> getData()
    {
        return data;
    }
}
