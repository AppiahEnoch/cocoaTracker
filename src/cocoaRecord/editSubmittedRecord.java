package cocoaRecord;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class editSubmittedRecord {

    ShareData s = ShareData.getInstance();
    String qry;
    Statement st;
    ResultSet rs;

    @FXML
    private Text txtBagsOverall;

    @FXML
    private Text txtKiloOverall;

    @FXML
    private TableColumn<ESD, String> col0;

    @FXML
    private Text txtName211;

    @FXML
    private Text txtSubmission;

    @FXML
    private Text txtSubmission1;

    @FXML
    private TableView<ESD> tbv;

    @FXML
    private TableColumn<ESD, String> col1;

    @FXML
    private TableColumn<ESD, String> col2;

    @FXML
    private TableColumn<ESD, String> col3;

    @FXML
    private BorderPane bp;

    @FXML
    private Text txtCtr;

    @FXML
    private Text txtBags;

    @FXML
    private Text txtKilo;
    Stage stage = new Stage();

    @FXML
    void closeWindow(ActionEvent event) {

        stage = (Stage) bp.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void initialize() {
     //   ShareData.farmerID = 2;

        col0.setCellValueFactory(new PropertyValueFactory<ESD, String>("counter"));
        col1.setCellValueFactory(new PropertyValueFactory<ESD, String>("userTime"));
        col2.setCellValueFactory(new PropertyValueFactory<ESD, String>("bag"));
        col3.setCellValueFactory(new PropertyValueFactory<ESD, String>("kilo"));


        col2.setCellFactory(TextFieldTableCell.forTableColumn());
        col3.setCellFactory(TextFieldTableCell.forTableColumn());

                   processValues();
        tbv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }
    void  processValues(){

        try {
            if (ShareData.farmerID > 0) {
                fillTable(ShareData.farmerID);
                txtName211.setText(ShareData.farmerName);
                txtSubmission1.setText(String.valueOf(ShareData.farmerID));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        double bags = 0;
        double kilos = 0;
        double ratio = 0;

        System.out.println(ShareData.farmerID);
        try {
            bags = sumFarmerBags(ShareData.farmerID);
            kilos = sumFarmerKilos(ShareData.farmerID);
            ratio = ShareData.acreBagRatio;
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtBags.setText(String.valueOf(dp1.format(bags)));
        txtKilo.setText(String.valueOf(dp1.format(kilos)));

        double bagOverall = bags + (kilos / ratio);
        double kiloOverall = kilos + (bags * ratio);

        txtBagsOverall.setText(String.valueOf(dp1.format(bagOverall)));
        txtKiloOverall.setText(String.valueOf(dp1.format(kiloOverall)));
        txtCtr.setText(String.valueOf(s.getCount(ShareData.farmerID)));
    }

    DecimalFormat dp1 = new DecimalFormat("0.0");

    void fillTable(long ID) {
        tbv.getItems().clear();
        try {
            s.openCon();
            st = s.conn.createStatement();
            String qry = "SELECT ctr,time,bag," +
                    "kilo from received where ID =" + ID + "";
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                String ctr = rs.getString("ctr");
                Date time = rs.getDate("time");
                String bag = rs.getString("bag");
                String kilo = rs.getString("kilo");


                tbv.getItems().add(new ESD(ctr, convertDateToWords(time), bag, kilo));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        s.closeConn();

    }


    double sumFarmerBags(long ID) {
        qry = "SELECT SUM(bag) as bagSum FROM received where ID = " + ID + "";
        double v = 0;

        try {
            s.openCon();
            ResultSet rs = s.conn.prepareStatement(qry).executeQuery();

            if (rs.next()) {
                v = rs.getDouble("bagSum");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        s.closeConn();
        return v;
    }


    double sumFarmerKilos(long ID) {
        qry = "SELECT SUM(kilo) as kiloSum FROM received where ID = " + ID + "";
        double v = 0;

        try {
            s.openCon();
            ResultSet rs = s.conn.prepareStatement(qry).executeQuery();

            if (rs.next()) {
                v = rs.getDouble("kiloSum");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        s.closeConn();
        return v;
    }


    String convertDateToWords(Date date) {


        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter = new SimpleDateFormat("E, dd MMM yyyy");
        String strDate = formatter.format(date);
        return strDate;
    }


    @FXML
    void deleteRecord(ActionEvent event) {
        deleteItem();
    }

    @FXML
    void deleteSelected(ActionEvent event) {

        Alert alert =
                new Alert(Alert.AlertType.WARNING,
                        "DO YOU REALLY WANT TO DELETE THE SELECTED RECORD?\n\n\t NB: \n YOU CANNOT UNDO THIS CHANGES\n AFTER" +
                                " DELETION.",
                        ButtonType.YES,
                        ButtonType.NO);
        alert.setTitle("DELETE SELECTED WARNING!");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.YES) {


            try {
                ESD colSelected1 = tbv.getSelectionModel().getSelectedItem();
                Object n = colSelected1.getCounter();
                Object ID = colSelected1.getBag();
                System.out.println("counter:" + n);
                System.out.println("bag:" + ID);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @FXML
    public void editBag(TableColumn.CellEditEvent editEvent) {

        ESD colSelected = tbv.getItems().get(editEvent.getTablePosition().getRow()).setBag(editEvent.getNewValue());

        ESD colSelected1 = tbv.getSelectionModel().getSelectedItem();
        Object n = colSelected1.getCounter();
        Object value = colSelected1.getBag();


        int id;
        String v = n.toString();
        String e = value.toString();

        id = Integer.parseInt(v);


        updateBag(id, e);

        processValues();

    }

    @FXML
    public void editKilo(TableColumn.CellEditEvent editEvent) {

        ESD colSelected = tbv.getItems().get(editEvent.getTablePosition().getRow()).setKilo(editEvent.getNewValue());

        ESD colSelected1 = tbv.getSelectionModel().getSelectedItem();
        Object n = colSelected1.getCounter();
        Object value = colSelected1.getKilo();


        int id;
        String v = n.toString();
        String e = value.toString();

        id = Integer.parseInt(v);


        updateKilo(id, e);

        processValues();
    }


    public void updateBag(int id, String value) {
        isBag = true;
        isKilo = false;

        s.openCon();
        System.out.println(qry);

        qry = "update received set bag = ? where ctr = ?;";

        try {
            PreparedStatement preparedStatement = s.conn.prepareStatement(qry);
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        s.closeConn();


    }

    boolean isBag = false;
    boolean isKilo = false;
    int currentID = 0;

    public void updateKilo(int id, String value) {
        isBag = false;
        isKilo = true;

        s.openCon();
        System.out.println(qry);

        qry = "update received set kilo = ? where ctr = ?;";

        try {
            PreparedStatement preparedStatement = s.conn.prepareStatement(qry);
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        s.closeConn();
    }


    void deleteItem() {

        ObservableList<ESD> colSelected1 = tbv.getSelectionModel().getSelectedItems();
        int size = colSelected1.size();

        if (size>0){


            String message = "ARE YOU SURE YOU WANT TO \n DELETE THE SELECTED RECORD?";


            if (size > 1) {
                message = "ARE YOU SURE YOU WANT TO \n DELETE ALL SELECTED RECORDS?";
            }

            Alert alert =
                    new Alert(Alert.AlertType.WARNING,
                            message,
                            ButtonType.YES,
                            ButtonType.NO);
            alert.setTitle("CONFIRM DELETION!");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.YES) {
                for (int i = 0; i < size; i++) {
                    ESD colSelected2 = colSelected1.get(i);
                    String ii = colSelected2.getCounter().trim();
                    deleteSpecificFromTable(Integer.parseInt(ii));
                }
            }

           processValues();
        }



    }

    public void deleteSpecificFromTable(int id) {
        s.openCon();
        try {
            qry = "DELETE FROM received where ctr= " + id + "";
            st = s.conn.createStatement();
            st.executeUpdate(qry);


        } catch (Exception e) {
            e.printStackTrace();


        }

        s.closeConn();
    }

    @FXML
    void checkey(KeyEvent event) {

        if (event.getCode().equals(KeyCode.DELETE)) {
            deleteItem();
        }

    }

void  openWindow(){
        Parent root;
    try {

        stage=(Stage)((bp)).getScene().getWindow();

        stage.close();

        stage.show();


    }
    catch (Exception e){
        e.printStackTrace();
    }
}


}