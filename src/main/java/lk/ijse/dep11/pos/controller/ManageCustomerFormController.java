package lk.ijse.dep11.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep11.pos.db.CustomerDataAccess;
import lk.ijse.dep11.pos.tm.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class ManageCustomerFormController {
    public AnchorPane root;
    public JFXTextField txtCustomerId;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerAddress;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public TableView<Customer> tblCustomers;
    public JFXButton btnAddNew;


    public void initialize(){

        String[] tblColumns = {"id","name","address"};
        for (int i = 0; i < tblColumns.length; i++) {
            tblCustomers.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(tblColumns[i]));
        }

        tblCustomers.getItems().addAll((CustomerDataAccess.getAllCustomers()));
        btnDelete.setDisable(true);
        txtCustomerId.setEditable(false);
        btnSave.setDefaultButton(true);
        btnAddNew.fire();
    }

    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/MainForm.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        Platform.runLater(primaryStage::sizeToScene);
    }

    public void btnAddNew_OnAction(ActionEvent actionEvent) {
        for (TextField textField : new TextField[]{txtCustomerId,txtCustomerName,txtCustomerAddress})
            textField.clear();
        tblCustomers.getSelectionModel().clearSelection();
        txtCustomerName.requestFocus();

        try {
            String lastCustomerId = CustomerDataAccess.getLastId();
            if (lastCustomerId == null){
                txtCustomerId.setText("C001");
            }else {
                int newId = Integer.parseInt(lastCustomerId.substring(1))+1;
                txtCustomerId.setText(String.format("C%03d",newId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
    }
}
