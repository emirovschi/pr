package com.emirovschi.pr.http;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HttpApplication extends Application
{
    private HttpController httpController;

    @Override
    public void start(final Stage primaryStage)
    {
        final JavafxHttpView httpView = new JavafxHttpView();
        httpController = new HttpController(httpView);
        final Scene scene = new Scene(httpView, 800, 600);

        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(200);
        primaryStage.setTitle("Http Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(final String[] args)
    {
        launch(args);
    }
}