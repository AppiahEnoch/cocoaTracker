package cocoaRecord;
import java.io.*;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.h2.tools.Server;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShareData {
    Statement st;
    ResultSet rs;
    String qry;

  static String imagePath=null;
  static int farmerID=0;
  static String farmerName="";
  static  double  acreBagRatio=0;


    private static ShareData instance;
    public int data = 0;

    private ShareData() {

    }

    synchronized public static ShareData getInstance() {
        if (instance == null) {
            instance = new ShareData();
        }
        return instance;
    }

    Connection conn;

    public Connection H2con() {
        try {
            conn = DriverManager.getConnection("jdbc:h2:~/cocoa",
                    "root", "root");
            st = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    void openCon() {
        try {
           H2con();
        } catch (Exception e) {
          //  e.printStackTrace();
        }

    }


    void closeConn() {
        try {
            conn.close();
        } catch (Exception e) {

        }
    }


    void dropDB() {
        openCon();
        try {
            String db = "DROP ALL OBJECTS DELETE FILES";
            st = conn.createStatement();
            st.execute(db);
            System.out.println("droped!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeConn();


    }


    public boolean createTables() {


        String farmer = "CREATE TABLE IF NOT EXISTS " +
                "farmer (id bigint auto_increment,fullName varchar(255),  DateOfBirth DATE," +
                "farmerTown varchar(255), mobile varchar(255), " +
                " farmSize NUMERIC(20, 2), expectedBags NUMERIC(20, 2),img BLOB(90000000k) )";



        String settings = "CREATE TABLE IF NOT EXISTS " +
                "settings(bagNum varchar(255))";

        String received = "CREATE TABLE IF NOT EXISTS " +
                "received(ctr bigint auto_increment Primary key, ID bigint, bag NUMERIC(20, 2) DEFAULT 0, kilo NUMERIC(20, 2) DEFAULT 0, " +
                "time timestamp  DEFAULT CURRENT_TIMESTAMP)";

        String department = "CREATE TABLE IF NOT EXISTS " +
                "department(abbrev varchar(255) primary key, name varchar(400))";


        String course = "CREATE TABLE IF NOT EXISTS " +
                "course(department varchar(200), code varchar(200) primary key, name  varchar(400))";

        String userView = "CREATE or replace view userData as select * from currentUser,grade,tbGPA";


        Statement st;

        try {

            openCon();
            st = conn.createStatement();
            st.execute(farmer);
            st.execute(settings);
            st.execute(received);
            //   st.execute(userView);

            closeConn();
            return true;
        } catch (Exception e) {
            e.printStackTrace();


        }


        return false;
    }


    public int getMaxID(String table, String column) {

        try {
           H2con();
            qry = "SELECT MAX( " + column + ") as ID from " + table + "";
            st = conn.createStatement();
            rs = st.executeQuery(qry);

            if (rs.next()) {
                int id = rs.getInt("ID");

                return id;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

       closeConn();
        return 0;

    }



    public void updateImage(String location){

        try {
            InputStream in = Main.class.getResourceAsStream("images/icons8_team_480px.png");
           // URL url = getClass().getResource("images/icons8_team_480px.png");
            InputStream inputstream=null;

            if (location==null){
                imageInsert(in);

                System.out.println("null location");
            }
            else {
                inputstream = new FileInputStream(location);
                imageInsert(inputstream);
                System.out.println("location: "+ShareData.imagePath);
            }





        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean updateImage(String location,String no){

        try {
            InputStream in = Main.class.getResourceAsStream("images/icons8_team_480px.png");
            // URL url = getClass().getResource("images/icons8_team_480px.png");
            InputStream inputstream=null;

            if (location==null){

              return false;
            }
            else {
                inputstream = new FileInputStream(location);
                imageInsert(inputstream);
            }





        } catch (Exception e) {
            e.printStackTrace();
        }
return true;
    }


    public  void  imageInsert(  InputStream in ){
        openCon();
        try {

            final String qry = "update farmer set img = ? where id = ?;";
            try {
                PreparedStatement st = conn.prepareStatement(qry);

                st.setBinaryStream(1,in);
                st.setInt(2,farmerID);
                st.executeUpdate();


            }
            catch (SQLException ex) {
             ex.printStackTrace();

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        closeConn();
    }


    public void deleteAllFromTable(String table) {
      openCon();
        try {
            qry = "DELETE FROM "+table+"";
            st = conn.createStatement();
            st.executeUpdate(qry);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();


        }

        closeConn();
    }


    public boolean userExist(String table, String column,String mobile) {

        try {
            H2con();
            qry = "SELECT ( " + column + ") as ID from " + table + "  where mobile= '"+mobile+"'";
            st = conn.createStatement();
            rs = st.executeQuery(qry);

            if (rs.next()) {
                String id = rs.getString("ID");
                System.out.println("exists: "+id);

                 return true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        closeConn();
        return false;

    }




  static   String pathToSecretFolder="";





    public Image getFarmerImage() {

              Image image1=null;
         Blob img = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        File file = null;

        try {
          openCon();

            st = conn.createStatement();
         //   String     qry = "SELECT img from farmer where mobile='"+mobile+"' or where ID= '"+ID+"'";

            String     qry = "SELECT img from farmer where ID="+farmerID+"";
            ResultSet     rs = st.executeQuery(qry);

            if (rs.next()) {
                inputStream = rs.getBinaryStream("img");
                System.out.println(66);

            }

            if (inputStream == null) {

                //  imv.setImage(null);
                return null;
            }

                       file=giveFileLocation();
            OutputStream os = new FileOutputStream(file);
            byte[] content = new byte[1024];

            while (inputStream.read(content) > 0) {
                os.write(content);
            }

             image1 = new Image(file.toURI().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return image1;
    }



    public File giveFileLocation() {
        boolean fileExist = false;
        String folderName="Farmer";
        String fileName = "farmer.png";
        String locationOnPC="Pictures";
        File userFile=null;
        try {

            // create a folder on user desktop
            String homeFolder = System.getProperty("user.home"); //path for folder
            Path path = Paths.get(homeFolder, locationOnPC,folderName );
            if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                // Folder is already created
            } else {
                // folder is being created
                Paths.get(homeFolder, locationOnPC, folderName).toFile().mkdir();
            }

            // user file will be stored in folder on the desktop.

             userFile = new File(String.valueOf(path), fileName);

            pathToSecretFolder =String.valueOf(path);
            System.out.println(pathToSecretFolder);

        }
        catch (Exception e){
            e.printStackTrace();
        }
       // userFile.deleteOnExit();

        return userFile;
    }



    int getCount(long ID){
        qry="SELECT COUNT(ID) as count FROM received where ID = "+ID+"";
        int v=0;

        try {
            openCon();
            ResultSet rs=conn.prepareStatement(qry).executeQuery();

            if(rs.next()){
               v =rs.getInt("count");
            }



        }
        catch (Exception e){
            e.printStackTrace();
        }
        closeConn();
        return v;
    }


    int getCount(String table){
        qry="SELECT COUNT(ID) as count FROM "+table+"";
        int v=0;

        try {
            openCon();
            ResultSet rs=conn.prepareStatement(qry).executeQuery();

            if(rs.next()){
                v =rs.getInt("count");
            }



        }
        catch (Exception e){
            e.printStackTrace();
        }
        closeConn();
        return v;
    }





}





