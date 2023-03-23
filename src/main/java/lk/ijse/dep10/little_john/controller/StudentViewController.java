package lk.ijse.dep10.little_john.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dep10.little_john.model.Student;

public class StudentViewController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Student> tblStudent;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    public void initialize(){
        Platform.runLater(btnNew::fire);
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        tblStudent.getSelectionModel().selectedItemProperty().addListener((val,pre,current)->{
            btnDelete.setDisable(current==null);
        });
    }

    @FXML
    void btnNewOnAction(ActionEvent event) {
        txtId.setText(generateId());
        txtAddress.clear();
        txtName.clear();
    }
    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }
    private String generateId(){
        if(tblStudent.getItems().isEmpty())return "S001";
        String id=tblStudent.getItems().get(tblStudent.getItems().size()-1).getId();
        int newId=Integer.parseInt(id.substring(1))+1;
        return String.format("S%3d",newId);
    }



}

