package com.emirovschi.pr.socket.common;

import com.emirovschi.pr.socket.common.data.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class DelimiterCommandHandler implements CommandHandler
{
    private static final int COMMAND_DELIMITER = 1;
    private static final String COMMAND_FIELD_DELIMITER = "\u0002";
    private static final String COMMAND_DATA_DELIMITER = "\u0003";
    private static final String COMMAND_DATA_VALUE_DELIMITER = "\u0004";

    private final CommandFactory commandFactory;

    public DelimiterCommandHandler(final CommandFactory commandFactory)
    {
        this.commandFactory = commandFactory;
    }

    @Override
    public <T extends Command> T read(final InputStream inputStream) throws IOException
    {
        final String command = readUntil(inputStream, COMMAND_DELIMITER);
        final String[] commandFields = command.split(COMMAND_FIELD_DELIMITER);

        final String commandType = commandFields[0];
        final Map<String, String> data = Stream.of(commandFields[1].split(COMMAND_DATA_DELIMITER))
            .map(row -> row.split(COMMAND_DATA_VALUE_DELIMITER))
            .collect(toMap(row -> row[0], row -> row[1]));

        return commandFactory.createCommand(commandType, data);
    }

    private String readUntil(final InputStream inputStream, final int end) throws IOException
    {
        int data;
        final List<Byte> bytes = new LinkedList<>();

        while ((data = inputStream.read()) != end && data != -1)
        {
            bytes.add((byte) data);
        }

        if (data == -1)
        {
            throw new IOException("Client disconnected");
        }

        final byte[] bytesArray = new byte[bytes.size()];
        IntStream.range(0, bytes.size()).forEach(i -> bytesArray[i] = bytes.remove(0));

        return new String(bytesArray);
    }

    @Override
    public void write(final OutputStream outputStream, final Command command) throws IOException
    {
        outputStream.write(command.getCommandType().getBytes());
        outputStream.write(COMMAND_FIELD_DELIMITER.getBytes());
        outputStream.write(serialize(command.getData()).getBytes());
        outputStream.write(COMMAND_DELIMITER);
        outputStream.flush();
    }

    private String serialize(final Map<String, String> data)
    {
        return data.entrySet().stream()
            .map(entry -> entry.getKey() + COMMAND_DATA_VALUE_DELIMITER + entry.getValue())
            .collect(joining(COMMAND_DATA_DELIMITER));
    }
}
