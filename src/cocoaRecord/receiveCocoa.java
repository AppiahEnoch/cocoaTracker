package cocoaRecord;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class receiveCocoa {
    private String farmerID;
    private String validColumn = "";
    private String userInput = "";
    ShareData s = ShareData.getInstance();
    double acreBagRatio = 0;
    @FXML
    private ImageView imvFarmer;
    @FXML
    private JFXTextField tfSearch;
    @FXML
    private Text txtID;

    @FXML
    private Text lbID;
    @FXML
    private CheckBox radioAllFields;

    @FXML
    private JFXTextField tfName;

    @FXML
    private JFXDatePicker dateP;

    @FXML
    private JFXTextField tfTown;

    @FXML
    private JFXTextField tfMobile;

    @FXML
    private JFXTextField tfSize;

    @FXML
    private JFXTextField tfBags;

    @FXML
    private Button btClear;

    @FXML
    private FontAwesomeIconView aesomeCan;

    @FXML
    private Button btSend;

    @FXML
    private Button btDetails;

    @FXML
    private FontAwesomeIconView aesomeUp;


    @FXML
    private TextField tfAmount;

    @FXML
    private Text txtName;

    @FXML
    private Text txtExpect;


    @FXML
    private Text txtSubmitted;


    @FXML
    private Text txtDiff;


    @FXML
    private Text txtSubmission;

    @FXML
    private RadioButton rk1;
    @FXML
    private RadioButton rk;

    DecimalFormat dp1=new DecimalFormat("0.0");

    @FXML
    void showDetails(ActionEvent event) {

      showTwoScenes(event,"editSubmittedRecord.fxml");


    }

    @FXML
    void clear(ActionEvent event) {
        tfName.clear();
        tfTown.clear();
        tfMobile.clear();
        tfSize.clear();
        tfBags.clear();
        tfSearch.clear();
        dateP.setValue(null);
        txtID.setText("###");

    }

    String oldMobile = "";


    @FXML
    void submit(ActionEvent event) {
        if (validateMobile()) {

            saveData();
            refreshPage();


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Record is Added successfully!");
            Image icon = new Image(getClass().getResourceAsStream("images/appp.png"));
            ImageView imageView = new ImageView();
            imageView.setImage(icon);

            alert.setGraphic(imageView);
            alert.showAndWait();


        }

           btDetails.setVisible(false);
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
    @FXML
    private Text txtKilos;

    @FXML
    void fetchDataFromDatabase(KeyEvent event) {
        setUpperCase(tfSearch);

        clearFields();
        boolean isvalid = false;
        if (getID()) {
            isvalid = true;
            System.out.println(1);
        } else if (getMobile()) {
            System.out.println(2);
            isvalid = true;
        } else if (getName()) {
            System.out.println(3);
            isvalid = true;
        }
        if (isvalid) {
            getDetailsFromDatabase();

            try {
                imvFarmer.setImage(s.getFarmerImage());
            } catch (Exception e) {
                e.printStackTrace();
            }

            btDetails.setVisible(true);
        }
        else {
            btDetails.setVisible(false);
        }

        if (ID>0){
          txtSubmission.setText(String.valueOf(s.getCount(ID)));
                     processValues();

                 hasFinish();


        }

    }

    DecimalFormat dp11=new DecimalFormat("0.0");
    double finalBags=0;
    double finalKilos=0;
    double ratioValue=64.0;
    double diff=0;
    double expectedBags;
    void processValues(){
        ratioValue=ShareData.acreBagRatio;
        try {
            double bags=0;
            String originalBag="";

            double kilos=0;


            try {

                bags=Double.parseDouble(getBagReceived(ID));
               originalBag =String.valueOf(bags);
                kilos=Double.parseDouble(getKiloReceived(ID));
            }
            catch (Exception e){

            }



            bags=(bags+0.0)*(ratioValue+0.0);

            double value=bags+kilos;
            finalKilos=value;
            finalBags=(value+0.0)/(ratioValue+0.0);

            String expect=getExpectedAmount(ID);
            double i=Double.parseDouble(expect);

            diff=i-finalBags;






            txtExpect.setText(expect);
            expectedBags=Double.parseDouble(expect);

            if (diff<0.2){
                diff=0;
            }

            double diffToKilo=diff*ratioValue;
            txtDiffKilo.setText(String.valueOf(dp1.format(diffToKilo)));

            txtDiff.setText(String.valueOf(dp11.format(diff)));
            txtSubmitted.setText(String.valueOf(dp11.format(finalBags)));
            txtKilos.setText(String.valueOf(dp11.format(finalKilos)));

            txRealBag.setText(originalBag);
            txtRealKilo.setText(String.valueOf(dp11.format(kilos)));




        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    private AnchorPane ac;
    public void initialize() {
        ShareData.acreBagRatio=s.getMaxID("settings","bagNum");
        ratioValue=ShareData.acreBagRatio;

        tfSearch.requestFocus();



        try {
Parent root;
Stage stage;
Scene scene;

            stage=(Stage)(ac).getScene().getWindow();
            scene=new Scene(ac);

            stage.setScene(scene);

        stage.setResizable(false);



        }
        catch (Exception e){

        }

    }

    String name;
    LocalDate dateOfBirth;
    String farmerTown;
    String mobile;
    double farmSize;


    @FXML
    void kiloCliked(MouseEvent event) {
        showTextField();
        tfAmount.setPromptText("Enter Number Of Kilos");

    }

    @FXML
    private Label lbChoose;

    @FXML
    void bagCliked(MouseEvent event) {
        showTextField();
        tfAmount.setPromptText("Enter Number Of Bags");
    }

    void showTextField() {
        lbChoose.setVisible(false);
        tfAmount.setVisible(true);
    }

    void hideTextField() {
        lbChoose.setVisible(true);
        tfAmount.setVisible(false);
    }

    void refreshPage() {
        rk.setSelected(false);
        rk1.setSelected(false);
        tfAmount.clear();
        tfSearch.clear();
        clearFields();
        hideTextField();
        txtKilos.setText("###");
        farmerID = null;
        ID = 0;
    }


    private void getData() {
        userInput = tfSearch.getText().trim();
    }


    double amount = 0;
    long ID;

    boolean validateMobile() {

        String n = tfAmount.getText().trim();
        if (n.isEmpty()) {
            return false;
        } else if (!checkNumeric(n)) {
            return false;
        }
        amount = Double.parseDouble(n);

        return true;
    }

    void playBeep() {
        try {

            Media sound = new Media(getClass().getResource("sounds/beeppp.mp3").toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Statement st;

    boolean getID() {
        getData();
        if (userInput.isEmpty()) {
            return false;
        } else if (!checkNumeric(userInput)) {
            return false;
        }

        try {
            s.openCon();
            st = s.conn.createStatement();
            //   String     qry = "SELECT img from farmer where mobile='"+mobile+"' or where ID= '"+ID+"'";
            String qry = "SELECT ID from farmer where ID='" + userInput + "'";
            ResultSet rs = st.executeQuery(qry);

            if (rs.next()) {
                farmerID = rs.getString("ID").trim();

                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        s.closeConn();
        return false;
    }

    boolean getMobile() {
        getData();
        if (userInput.isEmpty()) {
            return false;
        } else if (!checkNumeric(userInput)) {
            return false;
        }

        try {
            s.openCon();
            st = s.conn.createStatement();
            //   String     qry = "SELECT img from farmer where mobile='"+mobile+"' or where ID= '"+ID+"'";
            String qry = "SELECT ID from farmer where mobile='" + userInput + "'";
            ResultSet rs = st.executeQuery(qry);

            if (rs.next()) {
                farmerID = rs.getString("ID").trim();

                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        s.closeConn();
        return false;
    }


    void setUpperCase(JFXTextField tf) {
        String v = tf.getText();
        if (v.length() >= 1) {
            v = v.toUpperCase();
            tf.setText(v);
            tf.positionCaret(v.length());

        }
        else {

        }

    }

    boolean getName() {
        getData();
        if (userInput.isEmpty()) {
            return false;
        }

        try {
            s.openCon();
            st = s.conn.createStatement();
            //   String     qry = "SELECT img from farmer where mobile='"+mobile+"' or where ID= '"+ID+"'";
            String qry = "SELECT ID from farmer where fullName LIKE '%" + userInput + "%'";
            ResultSet rs = st.executeQuery(qry);

            if (rs.next()) {
                farmerID = rs.getString("ID").trim();

                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        s.closeConn();
        return false;
    }


    boolean getDetailsFromDatabase() {
        ShareData.farmerID = Integer.parseInt(farmerID);

        ID = ShareData.farmerID;

        try {
            s.openCon();
            st = s.conn.createStatement();
            //   String     qry = "SELECT img from farmer where mobile='"+mobile+"' or where ID= '"+ID+"'";
            String qry = "SELECT ID,fullName,DateOfBirth," +
                    "farmerTown,mobile,farmSize,expectedBags " +
                    "from farmer where ID =" + farmerID + "";
            ResultSet rs = st.executeQuery(qry);

            if (rs.next()) {
                txtID.setText(rs.getString("ID"));
                txtName.setText(rs.getString("fullName"));
                ShareData.farmerName=rs.getString("fullName");



                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        s.closeConn();
        return false;
    }


    public static final LocalDate NOW_LOCAL_DATE(String DOB) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(DOB, formatter);
        return localDate;
    }

    @FXML
    private Text txtRealKilo;

    @FXML
    private Text txRealBag;

    void clearFields() {
        txtName.setText("");
        txtID.setText("###");
        txtExpect.setText("###");
        txtSubmitted.setText("###");
        txtDiff.setText("###");
        txtSubmission.setText("#");
        txtKilos.setText("###");
        imvFarmer.setImage(null);
        txtRealKilo.setText("##");
        txRealBag.setText("##");
        txtDiffKilo.setText("00");

        farmerID=null;
        ID=0;

    }


    String qry;
    Connection conn;


    void saveKilos() {
        insertKilo();

    }

    void saveBags() {
        insertBag();

    }


    void saveData() {

        if (ID > 0) {
            if (rk.isSelected()) {
                saveKilos();
            } else if (rk1.isSelected()) {
                saveBags();
            }
            playBeep();
        }
        ID=0;

    }


    public void insertKilo() {
        Statement st;
        String qry;
        Connection conn;
        try {
            s.openCon();
            conn = s.conn;
            qry = "INSERT  INTO received (ID,kilo) values(+" + ID + "," + amount + ")";

            st = conn.createStatement();
            st.executeUpdate(qry);
            System.out.println("RECORD IS SAVED");

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();


        }

        s.closeConn();

        //System.out.println("path:"+ShareData.imagePath);
    }

    public void insertBag() {
        Statement st;
        String qry;
        Connection conn;
        try {
            s.openCon();
            conn = s.conn;
            qry = "INSERT  INTO received (ID,bag) values(+" + ID + "," + amount + ")";

            st = conn.createStatement();
            st.executeUpdate(qry);
            System.out.println("RECORD IS SAVED");

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();


        }

        s.closeConn();

        //System.out.println("path:"+ShareData.imagePath);
    }



    String getExpectedAmount(long ID){
        qry="SELECT expectedBags as ex  FROM farmer where ID = "+ID+"";
        String v="";

        try {
            s.openCon();
            conn=s.conn;
            ResultSet rs=conn.prepareStatement(qry).executeQuery();

            if(rs.next()){
                v =rs.getString("ex");
            }



        }
        catch (Exception e){
            e.printStackTrace();
        }
        s.closeConn();


        return v;


    }


    String getBagReceived(long ID){
        qry="SELECT sum(bag) as ex  FROM received where ID = "+ID+"";
        String v="0";

        try {
            s.openCon();
            conn=s.conn;
            ResultSet rs=conn.prepareStatement(qry).executeQuery();

            if(rs.next()){
                v =rs.getString("ex");
            }



        }
        catch (Exception e){
            e.printStackTrace();
        }

        s.closeConn();
        return v;
    }

    String getKiloReceived(long ID){
        qry="SELECT sum(kilo) as ex  FROM received where ID = "+ID+"";
        String v="0";

        try {
            s.openCon();
            conn=s.conn;
            ResultSet rs=conn.prepareStatement(qry).executeQuery();

            if(rs.next()){
                v =rs.getString("ex");
            }



        }
        catch (Exception e){
            e.printStackTrace();
        }
        s.closeConn();
        return v;
    }
    boolean hasFinish(){

        if(ID!=0){
            try{

                if (expectedBags<=finalBags){

                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("FARMER HAS FINISHED! \nCOCOA SUBMISSION");
                    Image icon = new Image(getClass().getResourceAsStream("images/appp.png"));
                    ImageView imageView=new ImageView();
                    imageView.setImage(icon);

                    alert.setGraphic(imageView);
                    alert.showAndWait();

                    return true;
                }


            }
            catch (Exception e){
                e.printStackTrace();
            }

        }


        return false;
    }

  double currentAmount=0;
    boolean hasLessThanAmountToPay(){

        if (ID!=0){
            try{

                double n=Double.parseDouble(tfAmount.getText().trim());
                currentAmount=n;
                if (rk.isSelected()){
                    n=(n/ratioValue);
                    currentAmount=n;
                }

                System.out.println("final bags: "+finalBags);
                System.out.println(n+finalBags);

                if ((n+finalBags)>expectedBags){
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("FARMER HAS LESS COCOA THAN\n THE CURRENT AMOUNT TO SUBMIT!");
                    Image icon = new Image(getClass().getResourceAsStream("images/appp.png"));
                    ImageView imageView=new ImageView();
                    imageView.setImage(icon);

                    alert.setGraphic(imageView);
                    alert.showAndWait();
                    return  true;

                }
            }
            catch (Exception e){

            }

        }



        return false;
    }


    @FXML
    void checkInput(KeyEvent event) {
         hasLessThanAmountToPay();

             changeFigures();


    }

    @FXML
    private Text txtDiffKilo;
    boolean changeFigures(){

        if (tfAmount.getText().trim().isEmpty()){
            processValues();
            return false;
        }

        if (currentAmount>0){
            double exp=Double.parseDouble(txtExpect.getText().trim());
            double p=Double.parseDouble(txtSubmitted.getText().trim());
            currentAmount=currentAmount+p;



            System.out.println("curr:"+currentAmount);
            double diff=exp-currentAmount;

            if (diff<0.1){
                diff=0;
            }

            double k=(diff*ratioValue);




            txtDiff.setText(String.valueOf(dp1.format(diff)));
            txtDiffKilo.setText(String.valueOf(dp1.format(k)));


        }

return true;
    }



    void showTwoScenes(ActionEvent event, String fxml){
        Stage st=new Stage();
        Scene scene;
        Parent r;

        try {
            r = FXMLLoader.load(getClass().getResource(fxml));



            st.setTitle("Cocoa Tracker");
            st.setScene(new Scene(r));


            st.setResizable(false);

            Image icon = new Image(getClass().getResourceAsStream("images/cocoalogo.png"));
            st.getIcons().add(icon);
            st.show();
        }
        catch (Exception e){
e.printStackTrace();
        }
    }

}

