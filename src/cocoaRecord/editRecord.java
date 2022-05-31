package cocoaRecord;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class editRecord {
    private String farmerID;
    private String validColumn="";
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
    private FontAwesomeIconView aesomeUp;

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

    String oldMobile="";

    @FXML
    void getImage(MouseEvent event) {
          fileOpener();
    }

    @FXML
    void submit(ActionEvent event) {
        if (!setCaret(tfName, dateP, tfTown, tfMobile, tfSize, tfBags)) {
            if (validateMobile()) {

                if (!mobileExist()){
                      updateRecord();
                     s.updateImage(ShareData.imagePath,"");

                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Record is Updated!");
                    Image icon = new Image(getClass().getResourceAsStream("images/appp.png"));
                    ImageView imageView=new ImageView();
                    imageView.setImage(icon);

                    alert.setGraphic(imageView);
                    alert.showAndWait();

                    tfSearch.clear();
                    clearFields();

                }



                // lbID.setText(String.valueOf(farmerID));


            }

        }


    }

    @FXML
    void checkName(KeyEvent event) {
        setUpperCase(tfName);
    }

    @FXML
    void checkTown(KeyEvent event) {
        setUpperCase(tfTown);
    }


    @FXML
    void checkSize(KeyEvent event) {


        if (checkNumeric(tfSize)) {
            tfBags.setText(getExpectedCocoaBags() + "\t Bags ");
        } else {
            tfBags.clear();
        }

    }


    @FXML
    void checkLocation(KeyEvent event) {
        setUpperCase(tfMobile);
    }

    @FXML
    void checkBgs(KeyEvent event) {
        checkNumeric(tfBags);
    }


    void setUpperCase(JFXTextField tf) {
        String v = tf.getText();
        if (v.length() > 1) {
            v = v.toUpperCase();
            tf.setText(v);
            tf.positionCaret(v.length());
        }

    }

    void setUpperCase2(JFXTextField tf) {
        String v = tf.getText();
        if (v.length() >= 1) {
            v = v.toUpperCase();
            tf.setText(v);
            tf.positionCaret(v.length());
        }

    }


    private boolean checkNumeric(JFXTextField tf) {
        String i = tf.getText().trim();

        if (i.isEmpty()) {
            return false;
        } else {
            try {
                double v = Double.parseDouble(i);
            } catch (Exception e) {
                tf.clear();
                return false;
            }
        }
        return true;
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


    boolean setCaret(JFXTextField tf1, JFXDatePicker d, JFXTextField tf2, JFXTextField tf3, JFXTextField tf4, JFXTextField tf5) {
        if (tf1.getText().trim().isEmpty()) {
            tf1.clear();
            tf1.requestFocus();
            return true;
        } else if (d.getValue() == null) {
            dateP.requestFocus();
            return true;
        } else if (tf2.getText().trim().isEmpty()) {
            tf2.clear();
            tf2.requestFocus();
            return true;
        } else if (tf3.getText().trim().isEmpty()) {
            tf3.clear();
            tf3.requestFocus();
            return true;
        } else if (tf4.getText().trim().isEmpty()) {
            tf4.clear();
            tf4.requestFocus();
            return true;
        } else if (tf5.getText().trim().isEmpty()) {
            tf5.clear();
            tf5.requestFocus();
            return true;
        }

        return false;
    }


    @FXML
    void fetchDataFromDatabase(KeyEvent event) {
         setUpperCase2(tfSearch);
          clearFields();
        boolean isvalid=false;
        if (getID()) {
            isvalid=true;
            System.out.println(1);
        } else if (getMobile()) {
            System.out.println(2);
            isvalid=true;
        }
        else if (getName()) {
            System.out.println(3);
            isvalid=true;
        }
        if (isvalid){
            getDetailsFromDatabase();

            try {
                imvFarmer.setImage(s.getFarmerImage());
            }
            catch (Exception e){
e.printStackTrace();
            }


        }
    }


    public void initialize() {

        setDateFormat();

        tfSearch.requestFocus();

    }

    String name;
    LocalDate dateOfBirth;
    String farmerTown;
    String mobile;
    double farmSize;
    double expectedBags;
    Blob img;


    void setDateFormat() {
        try {
            String pattern = "dd-MM-yyyy";

            dateP.setPromptText(pattern.toLowerCase());

            dateP.setConverter(new StringConverter<LocalDate>() {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);


                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }


                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getData() {
        userInput = tfSearch.getText().trim();
        name = tfName.getText().trim();
        dateOfBirth = dateP.getValue();
        farmerTown = tfTown.getText().trim();
        mobile = tfMobile.getText().trim();
      //  farmSize=Double.parseDouble(tfSize.getText().trim());
      //  expectedBags=Double.parseDouble(tfBags.getText().trim());
    }


    boolean validateMobile() {
        String mobile = tfMobile.getText().trim();
        int len = mobile.length();
        if (len != 10) {
            tfMobile.requestFocus();
            return false;
        }

        if (!checkNumeric(mobile)) {
            tfMobile.requestFocus();
            return false;
        }
        return true;
    }

    String numberOfBags;

    String getExpectedCocoaBags() {
        acreBagRatio = s.getMaxID("settings", "bagNum");
        double farmSize = 0;

        try {
            farmSize = Double.parseDouble(tfSize.getText().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }


        double bags = farmSize * acreBagRatio;
        expectedBags=bags;
        numberOfBags = String.valueOf(bags);
        return numberOfBags;
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
                farmerID= rs.getString("ID").trim();

                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        s.closeConn();
        return false;
    }


    boolean getDetailsFromDatabase() {
ShareData.farmerID=Integer.parseInt(farmerID);

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
                tfName.setText(rs.getString("fullName"));
                tfMobile.setText(rs.getString("mobile"));
                oldMobile=rs.getString("mobile");
                tfTown.setText(rs.getString("farmerTown"));
                tfBags.setText(rs.getString("expectedBags"));
                tfSize.setText(rs.getString("farmSize"));

                String date=rs.getString("DateOfBirth");
                System.out.println(date);

                   dateP.setValue(NOW_LOCAL_DATE(date));

                   playBeep();





                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        s.closeConn();
        return false;
    }


    public static final LocalDate NOW_LOCAL_DATE (String DOB){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(DOB , formatter);
        return localDate;
    }

    void  clearFields(){
        tfName.clear();
        tfTown.clear();
        tfMobile.clear();
        tfSize.clear();
        tfBags.clear();
        dateP.setValue(null);
        txtID.setText("###");
        imvFarmer.setImage(null);

    }





     String qry;
    Connection conn;
    public void updateRecord(){
        getData();
        farmSize=Double.parseDouble(tfSize.getText().trim());

      s.openCon();
        System.out.println(qry);

        qry="update farmer set fullName = ?,DateOfBirth = ?,farmerTown = ?,mobile = ?," +
                "farmSize = ?,expectedBags = ? where id = ?;";

        conn=s.conn;
        try {
            PreparedStatement preparedStatement =conn.prepareStatement(qry) ;
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, String.valueOf(dateOfBirth));
            preparedStatement.setString(3,farmerTown);
            preparedStatement.setString(4,mobile);
            preparedStatement.setDouble(5,farmSize);
            preparedStatement.setDouble(6, expectedBags);
            preparedStatement.setInt(7, Integer.parseInt(farmerID));

                preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }


s.closeConn();
    }




    void fileOpener() {

        Stage st = new Stage();
        FileChooser fileChooser = new FileChooser();

        File selectedFile = fileChooser.showOpenDialog(st);
        Image image = new Image(selectedFile.toURI().toString());


        System.out.println(image.isError());


        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("png", "*.png")
                , new FileChooser.ExtensionFilter("jpg", "*.jpg")

        );


        if (!image.isError()) {
            imvFarmer.setFitHeight(150);
            imvFarmer.setFitWidth(200);
            imvFarmer.setPreserveRatio(true);
            imvFarmer.setImage(image);
            ShareData.imagePath = selectedFile.getPath();


        }
    }



    boolean mobileExist() {
        getData();

        System.out.println(333);
        try {
            s.openCon();
            st = s.conn.createStatement();
            //   String     qry = "SELECT img from farmer where mobile='"+mobile+"' or where ID= '"+ID+"'";
            System.out.println("mobile:"+mobile);
            String qry = "SELECT ID from farmer where mobile ='" + mobile + "'";
            ResultSet rs = st.executeQuery(qry);
            System.out.println(3334);
            if (rs.next()) {
                System.out.println(335);
                int r=rs.getInt("ID");
                System.out.println("r:"+r);

                int rr=Integer.parseInt(farmerID);
                System.out.println("RR:"+rr);

                if (rr==r){
                    System.out.println(3338);
                    return false;
                }


                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Mobile Number is already Used\n by Another User!");
                alert.setTitle("Mobile is already Used");
             Image icon = new Image(getClass().getResourceAsStream("images/icons8_snail_48px.png"));
                ImageView imageView=new ImageView();
                imageView.setImage(icon);
                alert.setGraphic(imageView);
                alert.showAndWait();
                tfMobile.setText(oldMobile);
                tfMobile.requestFocus();

              return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        s.closeConn();
        return false;
    }





}
