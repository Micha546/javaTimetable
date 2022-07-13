package Main;

import Main.Controllers.MainController;
import Main.Logic.MainLogic;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = this.getClass().getResource("/resources/FXML/main.fxml");

            fxmlLoader.setLocation(url);

            Parent parent = fxmlLoader.load(url.openStream());
            MainController testController = fxmlLoader.getController();

            Scene scene = new Scene(parent);

            primaryStage.setScene(scene);

            primaryStage.show();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
