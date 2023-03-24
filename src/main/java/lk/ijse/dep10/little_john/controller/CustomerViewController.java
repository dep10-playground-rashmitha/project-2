package lk.ijse.dep10.little_john.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.dep10.little_john.db.DBConnection;
import lk.ijse.dep10.little_john.model.Customer;

import java.sql.*;

public class CustomerViewController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewCustomer;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Customer> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    public void initialize() {
        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        loadAllCustomer();

        btnDelete.setDisable(true);
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((ov, previous, current) -> {
            btnDelete.setDisable(current == null);
        });
        Platform.runLater(btnNewCustomer::fire);
    }

    private void loadAllCustomer() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rstCustomer = stm.executeQuery("SELECT *FROM Customers");
            while (rstCustomer.next()) {
                String id = rstCustomer.getString("id");
                String name = rstCustomer.getString("name");
                String address = rstCustomer.getString("address");

                Customer customer = new Customer(id, name, address);
                tblCustomer.getItems().add(customer);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customers").show();
        }


    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            int rstCustomer = stm.executeUpdate("DELETE FROM Customers WHERE id='"+tblCustomer.getSelectionModel().getSelectedItem().getId()+"'");



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tblCustomer.getItems().remove(tblCustomer.getSelectionModel().getSelectedItem());


    }

    @FXML
    void btnNewCustomer(ActionEvent event) {
        txtName.setDisable(false);
        txtAddress.setDisable(false);
        btnSave.setDisable(false);
        ObservableList<Customer> customerList = tblCustomer.getItems();
        var newId = customerList.isEmpty() ? "C001" :
                String.format("C%03d",Integer.parseInt(customerList.get(customerList.size() - 1).getId().substring(1)) + 1);
        txtId.setText( newId + "");
        txtName.clear();
        txtAddress.clear();

        tblCustomer.getSelectionModel().clearSelection();
        txtName.requestFocus();


    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!isDataValid()) return;
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement stmCustomer = connection
                    .prepareStatement("INSERT INTO Customers (id, name, address) VALUES (?, ?, ?)");
            stmCustomer.setString(1, txtId.getText());
            stmCustomer.setString(2, txtName.getText());
            stmCustomer.setString(3, txtAddress.getText());
            stmCustomer.executeUpdate();



            connection.commit();

            Customer customer = new Customer(txtId.getText(),
                    txtName.getText(),
                    txtAddress.getText());
            tblCustomer.getItems().add(customer);

            btnNewCustomer.fire();
        } catch (Throwable e) {
            try {
                DBConnection.getInstance().getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save the customer, try again!").show();
        } finally {
            try {
                DBConnection.getInstance().getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private boolean isDataValid() {
        boolean dataValid = true;

        String name = txtName.getText();
        String address = txtAddress.getText();

        if (address.strip().length() < 3) {
            txtAddress.requestFocus();
            txtAddress.selectAll();
            dataValid = false;
        }

        if (!name.matches("[A-Za-z ]+")) {
            txtName.requestFocus();
            txtName.selectAll();
            dataValid = false;
        }

        return dataValid;
    }

    @FXML
    void tblCustomer(MouseEvent event) {

    }

}
