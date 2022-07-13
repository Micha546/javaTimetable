package Main.Logic;

import Main.Engine.Evolution.Algorithm;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;

import java.io.File;

public class FileSavingLogic {
    public static String handleFileSaving(MenuBar menuParent, boolean isAlgorithmLoaded)
    {
        if(!isAlgorithmLoaded)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There is no algorithm loaded!");
            alert.showAndWait();
            return null;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ALSF files", "*.alsf"));

        File fileToSaveTo = fileChooser.showSaveDialog(menuParent.getScene().getWindow());

        return fileToSaveTo == null ? null : fileToSaveTo.getAbsolutePath();
    }
}
