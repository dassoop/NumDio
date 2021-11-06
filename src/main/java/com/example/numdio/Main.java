package com.example.numdio;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        Parent root = (FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml"))));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
        stage.setResizable(false);

        //set stage and scene to transparent. Also set bottom layer to transparent in css
//		stage.initStyle(StageStyle.TRANSPARENT);
//		scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.setTitle("Num-dio");
        stage.show();
        stage.setOpacity(0.99);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
