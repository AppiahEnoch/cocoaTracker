package cocoaRecord;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    ShareData s=ShareData.getInstance();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
       // root = FXMLLoader.load(getClass().getResource("registerFarmer.fxml"));
        root = FXMLLoader.load(getClass().getResource("mainPage.fxml"));
      // root = FXMLLoader.load(getClass().getResource("receiveCocoa.fxml"));
     //   root = FXMLLoader.load(getClass().getResource("idPage.fxml"));
     //  root = FXMLLoader.load(getClass().getResource("editSubmittedRecord.fxml"));
        primaryStage.setTitle("Cocoa Tracker");
        primaryStage.setScene(new Scene(root));
        Image icon = new Image(getClass().getResourceAsStream("images/cocoalogo.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.show();

           s.H2con();
        //  s.dropDB();
           s.createTables();

        ShareData.acreBagRatio=s.getMaxID("settings","bagNum");



    }


    public static void main(String[] args) {
        launch(args);
    }
}
