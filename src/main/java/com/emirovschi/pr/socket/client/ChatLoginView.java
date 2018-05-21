package com.emirovschi.pr.socket.client;

import java.util.function.Consumer;

public interface ChatLoginView
{
    void onSubmit(Consumer<String> onSubmit);

    void reset();
}
