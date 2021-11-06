package com.example.numdio;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class DraggableFileCell extends ListCell<File>
{
    private String cellString = null;

    //Contructor
    public DraggableFileCell()
    {
        ListCell<File> thisCell = this;

        setOnDragDetected(event ->
        {
            if(getItem() == null) return;

//			ObservableList<File> items = getListView().getItems();

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

            List<File> files = new ArrayList<File>(Arrays.asList(getItem()));
            content.putFiles(files);

            //Set icon of file while dragging with mouse
//			dragboard.setDragView(this.snapshot(null, null));
            dragboard.setDragView(null);
            dragboard.setContent(content);

            event.consume();
        });

        setOnMouseClicked(event ->
        {
            if(getItem() == null) return;
            event.consume();
        });

        //Set opacity lower when dragging mouse over cell
        setOnDragEntered(event ->
        {
            if(event.getGestureSource() != thisCell && event.getDragboard().hasFiles())
            {
                setOpacity(0.3);
            }
        });

        //Set opacity higher when finished dragging mouse off cell
        setOnDragExited(event ->
        {
            if(event.getGestureSource() != thisCell && event.getDragboard().hasFiles())
            {
                setOpacity(1);
            }
        });

        //Set the cell to drop on when released
        setOnDragOver(event ->
        {
            if (event.getGestureSource() != thisCell && event.getDragboard().hasFiles())
            {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });

        //On drop event
        setOnDragDropped(event ->
        {
            if(getItem() == null) return;

            Dragboard db = event.getDragboard();
            boolean success = false;

            if(db.hasFiles())
            {
                ObservableList<File> items = getListView().getItems();
                int origin = items.indexOf(db.getFiles().get(0));
                int destination = items.indexOf(getItem());

                //move origin value
                items.remove(origin);
                items.add(destination, db.getFiles().get(0));

                ListView<File> listView = getListView();
                listView.getSelectionModel().select(items.get(destination));
            }
            event.setDropCompleted(success);

            event.consume();
        });

        setOnDragDone(DragEvent::consume);
    }


    @Override
    protected void updateItem(File item, boolean empty)
    {
        //Run the super class update
        super.updateItem(item, empty);
        if(empty || item == null)
        {
            setText(null);
        }

        //get list from list view
        ObservableList<File> items = getListView().getItems();

        if(item != null)
        {

            cellString = items.get(getIndex()).getName();
            setText(cellString);
        }
    }
}

