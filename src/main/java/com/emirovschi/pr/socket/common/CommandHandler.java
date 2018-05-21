package com.emirovschi.pr.socket.common;

import com.emirovschi.pr.socket.common.data.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface CommandHandler
{
    <T extends Command> T read(InputStream inputStream) throws IOException;

    void write(OutputStream outputStream, Command command) throws IOException;
}
