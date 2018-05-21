package com.emirovschi.pr.http.data;

import lombok.Data;

import java.util.Map;

@Data
public class Response
{
    private final Map<String, String> headers;
    private final String body;
}
