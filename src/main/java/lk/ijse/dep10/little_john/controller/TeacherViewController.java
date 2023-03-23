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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lk.ijse.dep10.little_john.db.DBConnection;
import lk.ijse.dep10.little_john.model.Teacher;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.*;

public class TeacherViewController {

    public TableView<Teacher> tblTeacher;
    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewTeacher;

    @FXML
    private Button btnSave;


    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    public void initialize() {
        System.out.println("initialize");
        btnDelete.setDisable(true);
        tblTeacher.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblTeacher.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblTeacher.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        LoadAllTeacher();
        tblTeacher.getSelectionModel().selectedItemProperty().addListener((ov, previous, current) -> {
            btnDelete.setDisable(current == null);
            if (current == null) return;

            txtName.setText(current.getName());
            txtId.setText(current.getId());
            txtAddress.setText(current.getAddress());
        });

        Platform.runLater(btnNewTeacher::fire);


    }

    private void LoadAllTeacher() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Teachers");
            while (rst.next()) {

                String id = rst.getString("id");
                String name = rst.getString("name");
                String address = rst.getString("address");
                Teacher student = new Teacher(id, name,address);
                tblTeacher.getItems().add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Teacher selectedTeacher = tblTeacher.getSelectionModel().getSelectedItem();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stmStudent = connection.prepareStatement("DELETE FROM Teachers WHERE id = ?");
            stmStudent.setString(1, selectedTeacher.getId());
            stmStudent.executeUpdate();

            tblTeacher.getItems().remove(selectedTeacher);
            if (tblTeacher.getItems().isEmpty()) btnNewTeacher.fire();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to delete the teacher").show();
        }
    }

    @FXML
    void btnNewTeacherOnAction(ActionEvent event) {

        txtAddress.clear();
        txtName.clear();
        txtId.setText(generateID());
        txtName.requestFocus();

    }

    private String generateID() {
        if (tblTeacher.getItems().isEmpty()) {
            return "T001";
        }else {
            String lastId = tblTeacher.getItems().get(tblTeacher.getItems().size() - 1).getId();
            return String.format("T%03d", Integer.parseInt(lastId.substring(2)) + 1);
        }


    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!isDataValid()) return;

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO Teachers(ID, NAME, ADDRESS) VALUES (?,?,?)");
            stm.setString(1, txtId.getText());
            stm.setString(2, txtName.getText());
            stm.setString(3, txtAddress.getText());
            stm.executeUpdate();

            Teacher teacher = new Teacher(txtId.getText(), txtName.getText(), txtAddress.getText());
            tblTeacher.getItems().add(teacher);
            btnNewTeacher.fire();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save the teacher").show();
        }


    }

    private boolean isDataValid(){
        boolean dataValid = true;
        String name = txtName.getText().strip();
        String address = txtAddress.getText().strip();

        if (address.isEmpty() || address.length() < 3 || !address.matches("[A-Za-z ]+")) {
            txtAddress.requestFocus();
            txtAddress.selectAll();
            dataValid=false;
        }

        if (name.isEmpty() || !name.matches("[A-Za-z ]+")) {
            txtName.requestFocus();
            txtName.selectAll();
            dataValid = false;
        }
        return dataValid;
    }

}
