package Main.Logic;

import Main.Engine.Evolution.Algorithm;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableFactory;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableManager;
import Main.Engine.Xml.XmlReader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;

import java.io.*;

public class FileLoadingLogic {

    public static Algorithm handleFileLoading(MenuBar menuParent, FileType fileType, boolean isAlgorithmLoaded)
    {
        if(isAlgorithmLoaded)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("You already have an algorithm loaded, " +
                    "loading a new one will erase all progress of the current algorithm, " +
                    "are you sure that you want to load a new algorithm?");
            java.util.Optional<ButtonType> result = alert.showAndWait();
            if(result.orElse(ButtonType.CANCEL) == ButtonType.CANCEL)
                return null;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                fileType == FileType.alsfFile
                        ? new FileChooser.ExtensionFilter("ALSF files", "*.alsf")
                        : new FileChooser.ExtensionFilter("XML files", "*.xml")
        );

        File fileToLoadFrom = fileChooser.showOpenDialog(menuParent.getScene().getWindow());

        if(fileToLoadFrom != null)
        {
            if(fileType == FileType.alsfFile)
                return loadFromAlsf(fileToLoadFrom);
            else
                return loadFromXml(fileToLoadFrom);
        }
        else
            return null;
    }

    private static Algorithm loadFromXml(File fileToLoadFrom)
    {
        Algorithm algorithm = null;
        Alert alert = new Alert(Alert.AlertType.NONE);

        try{
            TimeTableManager manager =
                    new TimeTableManager(XmlReader.createETTDescriptorFromXmlFile(fileToLoadFrom.getAbsolutePath()));
            Algorithm temp = new Algorithm(new TimeTableFactory(), manager);
            algorithm = temp;

            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Algorithm loaded successfully!");
        }
        catch (RuntimeException ex)
        {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText(ex.getMessage());
        }
        finally {
            alert.show();
        }

        return algorithm;
    }

    private static Algorithm loadFromAlsf(File fileToLoadFrom)
    {
        Algorithm algorithm = null;
        Alert alert = new Alert(Alert.AlertType.NONE);

        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileToLoadFrom));

            Algorithm temp = (Algorithm) in.readObject();
            algorithm = temp;

            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Algorithm loaded successfully!");
        }
        catch(FileNotFoundException ex)
        {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("The path provided was illegal " + ex.getMessage());
        }
        catch(ClassNotFoundException | IOException ex)
        {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("The save file is too old/ is corrupted! (was saved before changes were made " +
                    "to the algorithm class)");
        } finally {
            alert.show();
        }

        return algorithm;
    }
}
