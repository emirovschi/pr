package com.emirovschi.pr.socket.client;

public class ChatController
{
    private final Client client;
    private final ChatView chatView;

    public ChatController(final Client client, final ChatView chatView)
    {
        this.client = client;
        this.chatView = chatView;

        chatView.onSend(this::send);
        client.onMessage(chatView::addMessage);
    }

    private void send(final String message)
    {
        client.send(message);
    }
}
