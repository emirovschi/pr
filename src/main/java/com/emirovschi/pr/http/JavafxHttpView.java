package com.emirovschi.pr.http;

import com.emirovschi.pr.http.data.HttpMethod;
import com.emirovschi.pr.http.data.Request;
import com.emirovschi.pr.http.data.Response;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.layout.HBox.setHgrow;

public class JavafxHttpView extends VBox implements HttpView
{
    public static final String SEPARATOR = ":";
    public static final String NEW_LINE = System.getProperty("line.separator");
    public static final String COURIER_NEW = "Courier New";

    private ChoiceBox<HttpMethod> requestMethod;
    private TextField requestUrl;
    private Button sendRequest;
    private TextArea requestHeaders;
    private TextArea requestBody;
    private TextArea responseValue;

    public JavafxHttpView()
    {
        requestMethod = new ChoiceBox<>();
        requestMethod.setItems(observableArrayList(HttpMethod.values()));
        requestMethod.setValue(HttpMethod.GET);

        requestUrl = new TextField("http://httpbin.org/get");
        requestUrl.setPromptText("Request url");

        sendRequest = new Button("Send");

        final HBox requestBox = new HBox();
        requestBox.setSpacing(20);
        requestBox.getChildren().add(requestMethod);
        requestBox.getChildren().add(requestUrl);
        requestBox.getChildren().add(sendRequest);
        setHgrow(requestUrl, Priority.ALWAYS);

        requestHeaders = new TextArea();
        requestHeaders.setFont(Font.font(COURIER_NEW));
        requestHeaders.setPromptText("Request headers" + NEW_LINE + "Rows are separated by new lines" + NEW_LINE + "Keys and values are separated by :");

        requestBody = new TextArea();
        requestBody.setFont(Font.font(COURIER_NEW));
        requestBody.setPromptText("Request body");

        responseValue = new TextArea();
        responseValue.setFont(Font.font(COURIER_NEW));
        responseValue.setEditable(false);
        responseValue.setWrapText(true);

        setPadding(new Insets(20));
        setSpacing(20);
        getChildren().add(requestBox);
        getChildren().add(requestHeaders);
        getChildren().add(requestBody);
        getChildren().add(responseValue);
        setVgrow(requestHeaders, Priority.SOMETIMES);
        setVgrow(requestBody, Priority.SOMETIMES);
        setVgrow(responseValue, Priority.ALWAYS);
    }

    @Override
    public void onSend(final ThrowableConsumer<Request, HttpException> request)
    {
        sendRequest.setOnAction(event -> sendRequest(request));
    }

    private void sendRequest(final ThrowableConsumer<Request, HttpException> request)
    {
        try
        {
            request.accept(buildRequest());
        }
        catch (HttpException exception)
        {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }

    private Request buildRequest()
    {
        final Map<String, String> headers = Stream.of(requestHeaders.getText().split("\n"))
                .filter(row -> row.contains(SEPARATOR))
                .collect(toMap(row -> substringBefore(row, SEPARATOR), row -> substringAfter(row, SEPARATOR)));

        return new Request(requestMethod.getValue(), requestUrl.getText(), headers , requestBody.getText());
    }

    private String substringBefore(final String value, final String separator)
    {
        return value.substring(0, value.indexOf(separator));
    }

    private String substringAfter(final String value, final String separator)
    {
        return value.substring(value.indexOf(separator), value.length());
    }

    @Override
    public void setResponse(final Response response)
    {
        final StringBuilder value = new StringBuilder();
        response.getHeaders().forEach((headerKey, headerValue) -> addHeader(headerKey, headerValue, value));
        value.append(NEW_LINE);
        value.append(response.getBody());

        responseValue.setText(value.toString());
    }

    private void addHeader(final String headerKey, final String headerValue, final StringBuilder value)
    {
        final String header = Stream.of(headerKey, headerValue)
                .filter(Objects::nonNull)
                .collect(joining(SEPARATOR));

        value.append(header);
        value.append(NEW_LINE);
    }
}
