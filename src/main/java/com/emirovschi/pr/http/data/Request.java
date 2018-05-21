package com.emirovschi.pr.http.data;

import lombok.Data;

import java.util.Map;

@Data
public class Request
{
    private final HttpMethod method;
    private final String url;
    private final Map<String, String> headers;
    private final String body;


}
