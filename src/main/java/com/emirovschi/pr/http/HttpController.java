package com.emirovschi.pr.http;

import com.emirovschi.pr.http.data.Request;
import com.emirovschi.pr.http.data.Response;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class HttpController
{
    public static final String DELIMITER = ",";

    public HttpController(final HttpView httpView)
    {
        httpView.onSend(request -> httpView.setResponse(send(request)));
    }

    private Response send(final Request request) throws HttpException
    {
        try
        {
            final HttpURLConnection con = getConnection(request);
            con.connect();
            return createResponse(con);
        }
        catch (final IOException e)
        {
            throw new HttpException("Could not connect to the server");
        }
    }

    private HttpURLConnection getConnection(final Request request) throws HttpException
    {
        try
        {
            final URL url = new URL(request.getUrl());
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod(request.getMethod().toString());
            request.getHeaders().forEach(con::setRequestProperty);

            if (request.getBody() != null && !request.getBody().isEmpty())
            {
                con.setDoOutput(true);
                IOUtils.write(request.getBody(), con.getOutputStream());
            }

            return con;
        }
        catch (final MalformedURLException e)
        {
            throw new HttpException("Bad url");
        }
        catch (final ProtocolException e)
        {
            throw new HttpException("Bad request http method");
        }
        catch (final IOException e)
        {
            throw new HttpException("Could not connect to the server");
        }
    }

    private Response createResponse(final HttpURLConnection con)
    {
        final Map<String, String> headers = con.getHeaderFields().entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().stream().collect(joining(DELIMITER))));

        return new Response(headers, readResponseBody(con));
    }

    private String readResponseBody(final HttpURLConnection con)
    {
        try
        {
            return IOUtils.toString(con.getInputStream());
        }
        catch (Throwable throwable)
        {
            return "";
        }
    }
}
