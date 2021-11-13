package com.example.numdio;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



public class MainController implements Initializable
{
    @FXML
    Button btnChooseFiles;
    @FXML
    Button btnClearFiles;
    @FXML
    Button btnNumberFiles;
    @FXML
    Button btnRemoveNumbers;
    @FXML
    Button btnCloseWindow;
    @FXML
    Button btnMinimizeWindow;

    @FXML
    TextField txtNumberInput;
    @FXML
    Label labelFileDestination;
    @FXML
    ListView<File> fileView = new ListView<File>();

    @FXML
    Pane windowDragBar = new Pane();

    RenameService renameService = new RenameService();

    private double xOffset;
    private double yOffset;

    public MainController()
    {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        //Set listview cell class to custom DraggableFileCell class
        fileView.setCellFactory(param -> new DraggableFileCell());


        //Set numberInput text input field to only allow numbers
        txtNumberInput.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue)
            {
                if (!newValue.matches("\\d*"))
                {
                    txtNumberInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Set events for undecorated window drag bar.
        windowDragBar.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        windowDragBar.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {

                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

    }



    //	**BUTTONS AND FUNCTIONS**
    public void chooseFiles(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();

        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();

        List<File> files = fileChooser.showOpenMultipleDialog(thisStage);
        if(files == null) return;
        fileView.getItems().clear(); // <-- Clear files in listview before adding new ones
        fileView.getItems().addAll(files);
        String fileDestination = renameService.setFileDestination(files.get(0));
        labelFileDestination.setText(fileDestination);

        event.consume();
    }

    public void clearFiles(ActionEvent event)
    {
        fileView.getItems().clear();
        labelFileDestination.setText(null);
        event.consume();
    }

    public void numberFiles(ActionEvent event)
    {
        if(fileView.getItems().size() <= 0) return;

        String txtInput = txtNumberInput.getText();
        int startingCount;
        if(txtNumberInput.getText() == "")
        {
            startingCount = 1;
        }
        else
        {
            startingCount = Integer.parseInt(txtInput);
        }

        File[] files = renameService.convertList(fileView.getItems());
        files = renameService.numberFiles(files, labelFileDestination.getText(), startingCount);
        fileView.getItems().setAll(files);
    }

    public void removeNumbers(ActionEvent event)
    {
        if(fileView.getItems().size() <= 0) return;
        File[] files = renameService.convertList(fileView.getItems());
        files = renameService.removeNumbers(files, labelFileDestination.getText());
        fileView.getItems().setAll(files);
    }

    public void removeNames(ActionEvent event)
    {
        if(fileView.getItems().size() <= 0) return;
        File[] files = renameService.convertList(fileView.getItems());
        files = renameService.removeNames(files, labelFileDestination.getText());
        fileView.getItems().setAll(files);
    }

    public void closeWindow(ActionEvent event)
    {
        Stage stage = (Stage) btnCloseWindow.getScene().getWindow();
        stage.close();
    }

    public void minimizeWindow(ActionEvent event)
    {
        Stage stage = (Stage) btnMinimizeWindow.getScene().getWindow();
        stage.toBack();
    }
}
