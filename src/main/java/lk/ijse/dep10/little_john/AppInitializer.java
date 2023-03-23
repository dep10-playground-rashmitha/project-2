package lk.ijse.dep10.little_john;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lk.ijse.dep10.little_john.db.DBConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try{
                System.out.println("Database connection is about to close.please check all are ok.");
                if(DBConnection.getInstance().getConnection()!=null &&
                        !DBConnection.getInstance().getConnection().isClosed()) {
                    DBConnection.getInstance().getConnection().close();
                }
            }catch (SQLException e){
                new Alert(Alert.AlertType.ERROR,"Something went Wrong.").showAndWait();
                throw new RuntimeException(e);
            }
        }));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            generateSchemaIfNotExist();
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainView.fxml"))));
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to load the login Form").showAndWait();
            throw new RuntimeException(e);
        }
        primaryStage.setTitle("Main Login Form");
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
    private void generateSchemaIfNotExist(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SHOW TABLES ");
            HashSet<String> tableNameSet=new HashSet<>();
            while (rst.next()){
                tableNameSet.add(rst.getString(1));
            }

            boolean tableExist = tableNameSet.containsAll(Set.of("Customers","Teachers","Students","Employees"));

            if(!tableExist){
                System.out.println("Some Tables are missing. so it created now missing tables");
                stm.execute(readDBScript());
            }

            System.out.println(tableExist);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String readDBScript(){
        InputStream is = getClass().getResourceAsStream("/schema.sql");
        try(BufferedReader br=new BufferedReader(new InputStreamReader(is))){
            String line;
            StringBuilder dbScriptBuilder=new StringBuilder();
            while ((line=br.readLine())!=null){
                dbScriptBuilder.append(line).append("\n");
            }
            return dbScriptBuilder.toString();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
