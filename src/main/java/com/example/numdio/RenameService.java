package com.example.numdio;
import java.io.File;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class RenameService
{
    //Static variable to access the system program is running on.
    private static String systemOS = System.getProperty("os.name").toLowerCase();


    public File[] numberFiles(File[] files, String fileDestination, int count)
    {
        //Check if list is already numbered, remove numbers if it is.
//		if(isNumbered(files[0].getName()))
//		{
//			System.out.println(files.toString());
//			files = removeNumbers(files, fileDestination);
//		}
        files = removeNumbers(files, fileDestination);

        //start count of files to be numbered
        int startingCount = count;
        File[] renamed = new File[files.length];
        for(int i = 0; i < files.length; i++)
        {
            String fileName = files[i].getName();
            String formatCount = String.format("%02d", startingCount); // <-- add leading zeros to count
            File rename = new File(fileDestination + formatCount + "_" + fileName); // <-- create new name to rename file to
            rename(files[i], rename); // <-- rename in file system
            renamed[i] = rename; //<-- add new name of file to renamed list of files
            startingCount++;
        }

        return renamed; // <-- return new list of renamed files
    }

    public boolean rename(File file, File rename)
    {
        if(file.renameTo(rename))
        {
            System.out.println("RENAME SUCCESSFUL");
            return true;
        }
        else
        {
            System.out.println("RENAME FAILED");
            return false;
        }
    }

    public File[] removeNumbers(File[] files, String fileDestination)
    {
//		//Check if first file is numbered
//		if(!isNumbered(files[0].getName()))
//		{
//			return files;
//		}

        File[] renamed = new File[files.length];
        for(int i = 0; i < files.length; i++)
        {
            String fileName = files[i].getName();
            if(isNumbered(fileName)) //<-- check if the first 2 digits of file name are numbers
            {
                int prefixIndex = fileName.indexOf("_"); //<-- start the prefix at the first underscore of the file name
                fileName = fileName.substring(prefixIndex + 1, fileName.length()); //<-- substring the file name starting after the first underscore
                File newFileName = new File(fileDestination + fileName);
                rename(files[i], newFileName); //<-- rename files in system
                renamed[i] = newFileName; //<-- add new name without numbering to new renamed list of files
            }
            else
            {
                renamed[i] = files[i];
            }
        }
        return renamed; //<-- return renamed list
    }

    public File[] removeNames(File[] files, String fileDestination)
    {
        File[] renamed = new File[files.length];

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Remove Names");
        alert.setHeaderText("Remove Names");
        alert.setContentText("This can not be undone, are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            if(isNumbered(files[0].getName()) && files[0].getName().contains("_"))
            {
                for(int i = 0; i < files.length; i++)
                {
                    String fileName = files[i].getName();
                    String extension = ".";

                    int underscore = fileName.indexOf("_"); //<-- start the prefix at the first underscore of the file name
                    int extensionIndex = fileName.indexOf(".");
                    extension = fileName.substring(extensionIndex, fileName.length());

                    fileName = fileName.substring(0, underscore); //<-- substring the file name starting after the first underscore
                    File newFileName = new File(fileDestination + fileName + extension);
                    rename(files[i], newFileName); //<-- rename files in system
                    renamed[i] = newFileName; //<-- add new name without numbering to new renamed list of files
                }
            }
        }
        else
        {
            renamed = files;
        }

        return renamed;
    }

    public boolean isNumbered(String fileName)
    {
        Boolean isNumbered = true;
        Character c;

        for(int i = 0; i < 2; i++)
        {
            c = fileName.charAt(i);

            if(!Character.isDigit(c))
            {
                isNumbered = false;
                return isNumbered;
            }
        }

        return isNumbered;
    }

    public File[] convertList(ObservableList<File> list)
    {
        int length = list.size();
        File[] files = new File[length];
        for(int i = 0; i < length; i++)
        {
            files[i] = list.get(i);
        }

        return files;
    }

    public String[] getFileNames(File[] files)
    {
        String[] fileNames = new String[files.length];

        for(int i = 0; i < files.length; i++)
        {
            fileNames[i] = files[i].getName();
        }
        return fileNames;
    }

    public String setFileDestination(File file)
    {
        File currFile = file;
        String destination = currFile.getParent();

        //for windows path
        if(systemOS.contains("win"))
        {
            destination = destination + "\\";
        }

        //for osx path
        if(systemOS.contains("mac"))
        {
            destination = destination + "/";
        }

        return destination;
    }

}

