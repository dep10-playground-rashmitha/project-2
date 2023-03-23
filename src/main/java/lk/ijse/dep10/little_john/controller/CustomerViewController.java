package lk.ijse.dep10.little_john.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;

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
