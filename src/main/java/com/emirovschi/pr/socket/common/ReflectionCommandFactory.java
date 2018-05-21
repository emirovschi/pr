package com.emirovschi.pr.socket.common;

import com.emirovschi.pr.socket.common.data.Command;

import java.util.Map;

public class ReflectionCommandFactory implements CommandFactory
{
    private static final String PACKAGE = ReflectionCommandFactory.class.getPackage().getName() + ".data.";

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Command> T createCommand(final String type, final Map<String, String> data)
    {
        try
        {
            return (T) Class.forName(PACKAGE + type).getConstructor(Map.class).newInstance(data);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Bad command type, expected [" + type + "]: " + e.getMessage());
        }
    }
}
