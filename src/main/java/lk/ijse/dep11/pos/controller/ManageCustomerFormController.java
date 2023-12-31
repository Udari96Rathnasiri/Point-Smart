package lk.ijse.dep11.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep11.pos.db.CustomerDataAccess;
import lk.ijse.dep11.pos.db.OrderDataAccess;
import lk.ijse.dep11.pos.tm.Customer;
import org.eclipse.jdt.internal.compiler.batch.Main;

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


    public void initialize() throws SQLException {

        String[] tblColumns = {"id","name","address"};
        for (int i = 0; i < tblColumns.length; i++) {
            tblCustomers.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(tblColumns[i]));
        }
        btnDelete.setDisable(true);
        txtCustomerId.setEditable(false);
        btnSave.setDefaultButton(true);
        btnAddNew.fire();

        tblCustomers.getItems().addAll((CustomerDataAccess.getAllCustomers()));

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((ov,prev,cur) -> {
            if (cur != null){
                btnSave.setText("UPDATE");
                btnDelete.setDisable(false);
                txtCustomerId.setText(cur.getId());
                txtCustomerName.setText(cur.getName());
                txtCustomerAddress.setText(cur.getAddress());
            }else {
                btnSave.setText("SAVE");
                btnDelete.setDisable(true);
            }
        });
        Platform.runLater(txtCustomerName::requestFocus);
    }

    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        MainFormController.navigateToMain(root);
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
        if (!isDataValid()) return;

        String id = txtCustomerId.getText().strip();
        String name = txtCustomerName.getText().strip();
        String address = txtCustomerAddress.getText().strip();
        Customer customer = new Customer(id, name, address);
        try {
            if(btnSave.getText().equals("SAVE")) {
                CustomerDataAccess.saveCustomer(customer);
                tblCustomers.getItems().add(customer);
            }else{
                CustomerDataAccess.updateCustomer(customer);
                ObservableList<Customer> customerList = tblCustomers.getItems();
                Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
                customerList.set(customerList.indexOf(selectedCustomer),customer);
                tblCustomers.refresh();

            }
            btnAddNew.fire();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to save the customer, try later!").show();
        }
    }

    private boolean isDataValid(){
        String name = txtCustomerName.getText().strip();
        String address = txtCustomerAddress.getText().strip();
        if (!name.matches("[A-Za-z ]{3,}")){
            txtCustomerName.requestFocus();
            txtCustomerName.selectAll();
            return false;
        }else if(address.length() < 3){
            txtCustomerAddress.requestFocus();
            txtCustomerAddress.selectAll();
            return false;
        }
        return true;
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        try {
            if (OrderDataAccess.existsOrderByCustomerId(txtCustomerId.getText())){
                new Alert(Alert.AlertType.ERROR,
                        "Unable to delete this customer, already associated with an order").show();
            }else{
                CustomerDataAccess.deleteCustomer(txtCustomerId.getText());
                ObservableList<Customer> customerList = tblCustomers.getItems();
                Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
                customerList.remove(selectedCustomer);
                if (customerList.isEmpty()) btnAddNew.fire();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
