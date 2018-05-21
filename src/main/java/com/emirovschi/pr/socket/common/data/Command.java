package com.emirovschi.pr.socket.common.data;

import java.util.Map;

public interface Command
{
    String getCommandType();

    Map<String, String> getData();
}
