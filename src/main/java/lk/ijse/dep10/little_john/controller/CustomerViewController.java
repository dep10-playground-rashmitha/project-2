package lk.ijse.dep10.little_john.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.dep10.little_john.db.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerViewController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewCustomer;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<?> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    public void initialize(){
        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        loadAllCustomer();
    }
    private void loadAllCustomer(){
        Connection connection= DBConnection.getInstance().getConnection();
        try {
            Statement stm = connection.createStatement();
            stm.executeQuery("SELECT *FROM ");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnNewCustomer(ActionEvent event) {

    }

    @FXML
    void btnSameOnAction(ActionEvent event) {

    }

    @FXML
    void tblCustomer(MouseEvent event) {

    }

}
