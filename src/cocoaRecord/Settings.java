package cocoaRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Settings {
    String qry;
    Statement st;
    ResultSet rs;
    ShareData s=ShareData.getInstance();
    Connection conn;

    String bagNum;

    @FXML
    private TextField tfAcreRatio;

    @FXML
    void updateSettings(ActionEvent event) {

      closeWindow(event,"settings.fxml");

    }


    public void initialize(){
        tfAcreRatio.setText(String.valueOf(s.getMaxID("settings","bagNum")));
    }

    @FXML
    void save(KeyEvent event) {
          insertBagNum();
    }

    public void insertBagNum() {
        bagNum=tfAcreRatio.getText().trim();

        if (checkNumeric(bagNum)){
            s.deleteAllFromTable("settings");


            s.openCon();
            conn=s.conn;
            try {
                qry = "INSERT  INTO settings (bagNum) values('" + bagNum + "')";
                st = conn.createStatement();
                st.executeUpdate(qry);
                System.out.println("inserted!");

            } catch (Exception e) {
                e.printStackTrace();
            }
            s.closeConn();

        }

        ShareData.acreBagRatio=s.getMaxID("settings","bagNum");
    }

    private boolean checkNumeric(String string) {
        String i = string;

        if (i.isEmpty()) {
            return false;
        } else {
            try {
                double v = Double.parseDouble(i);
            } catch (Exception e) {

                return false;
            }
        }
        return true;
    }



    void closeWindow(ActionEvent event, String fxml){

        Stage currentStage;
        Scene currentScene;
        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            currentStage=(Stage)((Node)event.getSource()).getScene().getWindow();
            currentScene=new Scene(root);

            currentStage.setScene(currentScene);
            currentStage.close();


        }
        catch (Exception e){

        }
    }
}
