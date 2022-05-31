package cocoaRecord;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import javax.swing.event.ChangeListener;

public class mainPage {


    @FXML
TabPane tp;
    Stage stage=new Stage();

    Parent root;
    Scene  scene;

public void initialize(){

}


@FXML
    void tabReceiveClicke(Event event){


    try {

        
                    stage=(Stage)((tp)).getScene().getWindow();
                         stage.setMaximized(false);
                         stage.setResizable(false);

    }
    catch (Exception e){
 e.printStackTrace();
    }

}



    @FXML
    void editCliked(Event event){

       stage.setResizable(true);
    }



    @FXML
    void newFarmerClicked(Event event){

 stage.setResizable(true);

    }

}
