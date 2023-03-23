package lk.ijse.dep10.little_john.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnStudent;

    @FXML
    private Button btnTeachers;

    public void initialize() {

    }

    @FXML
    void btnCustomersOnAction(ActionEvent event) {

    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/EmployeeView.fxml"))));
        stage.setTitle("Employee Form");
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    void btnStudentOnAction(ActionEvent event) throws IOException {
        Stage studentStage=new Stage();
        studentStage.initModality(Modality.WINDOW_MODAL);
        studentStage.initOwner(btnStudent.getScene().getWindow());
        studentStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/StudentView.fxml"))));
        studentStage.setTitle("Student Form");
        studentStage.setResizable(false);
        studentStage.show();
        studentStage.centerOnScreen();

    }

    @FXML
    void btnTeachersOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/TeacherView.fxml"))));
        stage.centerOnScreen();
        stage.show();
    }

}

