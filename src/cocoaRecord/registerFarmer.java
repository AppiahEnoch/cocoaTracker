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

import java.io.File;

import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class registerFarmer {
    private int farmerID;
    ShareData s = ShareData.getInstance();
    double acreBagRatio=0;
    @FXML
    private ImageView imvFarmer;

    @FXML
    private Text txtFarmerCount;
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

    }

    @FXML
    void getImage(MouseEvent event) {
        fileOpener();
    }
    @FXML
    void submit(ActionEvent event) {
        if (!setCaret(tfName, dateP, tfTown, tfMobile, tfSize, tfBags)) {
            if (validateMobile()){
                getData();
                farmerID = s.getMaxID("farmer", "ID");
                ShareData.farmerID = farmerID;
                // lbID.setText(String.valueOf(farmerID));
                s.updateImage(ShareData.imagePath);

                showTwoScenes(event,"idPage.fxml");



            }
        }
        txtFarmerCount.setText(String.valueOf(s.getCount("farmer")));

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


        if (checkNumeric(tfSize)){
           tfBags.setText( getExpectedCocoaBags()+"\t Bags ");
        }
        else {
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


    boolean setCaret(JFXTextField tf1,  JFXTextField tf3) {
        if (tf1.getText().trim().isEmpty()) {
            tf1.clear();
            tf1.requestFocus();
            return true;
        }
        else if (tf3.getText().trim().isEmpty()) {
            tf3.clear();
            tf3.requestFocus();
            return true;
        }


        return false;
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

            System.out.println(ShareData.imagePath);
        }
    }


    @FXML
    void openSettings(ActionEvent event) {
       showTwoScenes(event,"settings.fxml");


    }


    public void initialize() {

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

    txtFarmerCount.setText(String.valueOf(s.getCount("farmer")));
    }

    String name;
    LocalDate dateOfBirth;
    String farmerTown;
    String mobile;
    double farmSize;
    double expectedBags;
    Blob img;


    private void getData() {
        name = tfName.getText().trim();
        //  DateFormat dateFormat=new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        dateOfBirth = dateP.getValue();
        farmerTown = tfTown.getText().trim();
        mobile = tfMobile.getText().trim();

        try {

            InputStream in = Main.class.getResourceAsStream("icons8_team_480px.png");
            URL url = getClass().getResource("icons8_team_480px.png");
            System.out.println("uri: " + url);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            farmSize = Double.parseDouble(tfSize.getText());
            expectedBags = Double.parseDouble(numberOfBags);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!s.userExist("farmer", "mobile", mobile)) {
            insert();
            playBeep();

            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Record is Saved!");
            Image icon = new Image(getClass().getResourceAsStream("images/appp.png"));
            ImageView imageView=new ImageView();
            imageView.setImage(icon);

            alert.setGraphic(imageView);
            alert.showAndWait();
        }

    }


    public void insert() {
        Statement st;
        String qry;
        Connection conn;
        try {
            s.openCon();
            conn = s.conn;
            qry = "INSERT  INTO farmer (fullName,DateOfBirth ,farmerTown,mobile, farmSize,expectedBags) values(" +
                    "'" + name + "','" + dateOfBirth + "','" + farmerTown + "','" + mobile + "' ,'" + farmSize + "', '" + expectedBags + "' " +
                    ")";
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


   boolean validateMobile(){
        String   mobile = tfMobile.getText().trim();
        int len=mobile.length();
        if (len!=10){
            tfMobile.requestFocus();
            return false;
        }

        if (!checkNumeric(mobile)){
            tfMobile.requestFocus();
            return false;
        }
        return true;
   }

String numberOfBags;
   String getExpectedCocoaBags(){
       acreBagRatio=s.getMaxID("settings","bagNum");
       ShareData.acreBagRatio=acreBagRatio;
        double farmSize=0;

        try {
            farmSize=Integer.parseInt(tfSize.getText().trim());
        }
        catch (Exception e){
            e.printStackTrace();
        }


        if (acreBagRatio==0){
            acreBagRatio=8;
            ShareData.acreBagRatio=acreBagRatio;
        }
        double bags=farmSize*acreBagRatio;
            numberOfBags=String.valueOf(bags);
        return numberOfBags;
   }


    void openWindowByClick(ActionEvent event, String fxml){
       Stage currentStage;
        Scene currentScene;
        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            currentStage=(Stage)((Node)event.getSource()).getScene().getWindow();
            currentScene=new Scene(root);

            currentStage.setScene(currentScene);

            currentStage.show();
            root.requestFocus();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            currentStage.setX((primScreenBounds.getWidth() -  currentStage.getWidth()) / 2);
            currentStage.setY((primScreenBounds.getHeight() -  currentStage.getHeight()) / 2);

        }
        catch (Exception e){

        }
    }




    void showTwoScenes(ActionEvent event, String fxml){
        Stage st=new Stage();
        Scene scene;
        Parent r;

        try {
            r = FXMLLoader.load(getClass().getResource(fxml));



            st.setTitle("Cocracker");
            st.setScene(new Scene(r));


            st.setResizable(false);

            Image icon = new Image(getClass().getResourceAsStream("images/cocoalogo.png"));
            st.getIcons().add(icon);
            st.show();
        }
        catch (Exception e){

        }
    }

    void playBeep(){
       try {

           Media sound = new Media(getClass().getResource("sounds/beeppp.mp3").toExternalForm());
           MediaPlayer mediaPlayer = new MediaPlayer(sound);
           mediaPlayer.play();
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }
}
