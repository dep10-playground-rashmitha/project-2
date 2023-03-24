package lk.ijse.dep10.little_john.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dep10.little_john.db.DBConnection;
import lk.ijse.dep10.little_john.model.Student;

import java.sql.*;

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

        loadStudents();

        tblStudent.getSelectionModel().selectedItemProperty().addListener((val,pre,current)->{
            btnDelete.setDisable(current==null);
            if(current==null)return;
            txtId.setText(current.getId());
            txtName.setText(current.getName());
            txtAddress.setText(current.getAddress());
        });
    }

    @FXML
    void btnNewOnAction(ActionEvent event) {
        txtId.setText(generateId());
        txtAddress.clear();
        txtName.clear();
        txtName.requestFocus();
        tblStudent.getSelectionModel().clearSelection();
    }
    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if(!isValid())return;
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO Students (id, name, address) VALUES (?,?,?)");
            stm.setString(1,txtId.getText());
            stm.setString(2,txtName.getText());
            stm.setString(3,txtAddress.getText());
            stm.executeUpdate();
            Student studentAdd = new Student(txtId.getText(), txtName.getText(), txtAddress.getText());
            tblStudent.getItems().add(studentAdd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id=txtId.getText();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE FROM Students WHERE id='"+id+"'");
            Student selectStudent = tblStudent.getSelectionModel().getSelectedItem();
            tblStudent.getItems().remove(selectStudent);
            if(tblStudent.getSelectionModel().isEmpty()){
                btnNew.fire();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    private String generateId(){
        if(tblStudent.getItems().isEmpty())return "S001";
        String id=tblStudent.getItems().get(tblStudent.getItems().size()-1).getId();
        int newId=Integer.parseInt(id.substring(1))+1;
        return String.format("S%03d",newId);
    }
    private void loadStudents(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement stm = connection.createStatement();
            String sql="SELECT * FROM Students";
            ResultSet rst = stm.executeQuery(sql);
            while (rst.next()){
                String id=rst.getString("id");
                String name=rst.getString("name");
                String address=rst.getString("address");
                tblStudent.getItems().add(new Student(id,name,address));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean isValid(){
        boolean dataValid=true;
        String name=txtName.getText();
        String address=txtAddress.getText();
        if(address.isEmpty() || address.length()<3 || !address.matches("[A-Za-z0-9 ]+")){
            txtAddress.requestFocus();
            txtAddress.selectAll();
            dataValid=false;
        }

        if(name.isEmpty()||!name.matches("[A-Za-z ]+")){
            txtName.requestFocus();
            txtName.selectAll();
            dataValid=false;
        }
        return dataValid;
    }



}

