package lk.ijse.dep10.little_john.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dep10.little_john.db.DBConnection;
import lk.ijse.dep10.little_john.model.Employee;

import java.sql.*;
import java.util.Arrays;

public class EmployeeViewController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewEmployee;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Employee> tblEmployee;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    public void initialize() {
        tblEmployee.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblEmployee.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblEmployee.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        loadAllEmployees();
        Platform.runLater(btnNewEmployee::fire);

        tblEmployee.getSelectionModel().selectedItemProperty().addListener((value, previous, current) -> {
            btnDelete.setDisable(current==null);
            if (current==null)return;

            txtId.setText(current.getId());
            txtName.setText(current.getName());
            txtAddress.setText(current.getAddress());
        });
    }

    private void loadAllEmployees() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Employees");

            while (rst.next()){
                String id = rst.getString("id");
                String name = rst.getString("name");
                String address = rst.getString("address");

                Employee employee = new Employee(id, name, address);
                tblEmployee.getItems().add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Employee selectedEmployee = tblEmployee.getSelectionModel().getSelectedItem();
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement("DELETE FROM Employees WHERE id=?");
            stm.setString(1,txtId.getText());
            stm.executeUpdate();
            connection.commit();

            tblEmployee.getItems().remove(selectedEmployee);
            if (tblEmployee.getItems().isEmpty())btnNewEmployee.fire();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void btnNewEmployeeOnAction(ActionEvent event) {
        generateStudentID();
        txtName.clear();
        txtAddress.clear();
        tblEmployee.getSelectionModel().clearSelection();
        txtName.requestFocus();
    }

    private void generateStudentID() {
        if (tblEmployee.getItems().isEmpty()){
            txtId.setText("E001");
            return;
        }

        Employee lastEmployee = tblEmployee.getItems().get(tblEmployee.getItems().size() - 1);
        String lastStudentId = lastEmployee.getId();
        String[] split = lastStudentId.split("E");
        int nextStudentId = Integer.parseInt(split[1].strip()) + 1;
        String newStudentId = String.format("E%03d", nextStudentId);
        txtId.setText(newStudentId);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!isDataValid())return;

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            PreparedStatement stm = connection.prepareStatement("INSERT INTO Employees (id,name,address) VALUES (?,?,?)");
            stm.setString(1,txtId.getText());
            stm.setString(2,txtName.getText());
            stm.setString(3,txtAddress.getText());
            stm.executeUpdate();
            connection.commit();

            Employee employee = new Employee(txtId.getText(), txtName.getText(), txtAddress.getText());
            tblEmployee.getItems().add(employee);
            btnNewEmployee.fire();
        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            try {
                DBConnection.getInstance().getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isDataValid() {
        boolean dataValid = true;

        if (!txtAddress.getText().matches("[A-Za-z0-9/ ]+")){
            txtAddress.requestFocus();
            txtAddress.selectAll();
            dataValid = false;
        }

        if (!txtName.getText().matches("[A-Za-z ]+")){
            txtName.requestFocus();
            txtName.selectAll();
            dataValid = false;
        }
        return dataValid;
    }

}
