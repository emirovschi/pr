package com.emirovschi.pr.socket.common;

import com.emirovschi.pr.socket.common.data.Command;

import java.util.Map;

public interface CommandFactory
{
    <T extends Command> T createCommand(final String type, final Map<String, String> data);
}
