package com.emirovschi.pr.semaphore;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.Semaphore;

public class SemaphoreApplication extends Application
{
    private static final int SEMAPHORE_MAX = 3;
    private static final boolean SEMAPHORE_FAIR = true;

    private SemaphoreController semaphoreController;

    @Override
    public void start(final Stage primaryStage)
    {
        final JavafxSemaphoreView semaphoreView = new JavafxSemaphoreView();
        final Semaphore semaphore = new Semaphore(SEMAPHORE_MAX, SEMAPHORE_FAIR);

        semaphoreController = new DefaultSemaphoreController(semaphoreView, semaphore);

        final Scene scene = new Scene(semaphoreView, 800, 600);

        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(200);
        primaryStage.setTitle("Semaphore");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> semaphoreController.stop());
    }

    public static void main(final String[] args)
    {
        launch(args);
    }
}
