package com.emirovschi.pr.http;

import com.emirovschi.pr.http.data.Request;
import com.emirovschi.pr.http.data.Response;

public interface HttpView
{
    void onSend(final ThrowableConsumer<Request, HttpException> request);

    void setResponse(Response response);
}