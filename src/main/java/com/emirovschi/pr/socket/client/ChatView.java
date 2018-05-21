package com.emirovschi.pr.socket.client;

import com.emirovschi.pr.socket.common.data.Message;

import java.util.function.Consumer;

public interface ChatView
{
    void setName(String name);

    void addMessage(Message message);

    void onSend(final Consumer<String> onSend);
}
