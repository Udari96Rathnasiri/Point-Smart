package lk.ijse.dep11.pos.controller;

import javafx.application.Platform;
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
import lk.ijse.dep11.pos.db.OrderDataAccess;
import lk.ijse.dep11.pos.tm.Order;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class SearchOrdersFormController {
    public TextField txtSearch;
    public TableView<Order> tblOrders;
    public AnchorPane root;


    public void initialize() throws IOException {
        String[] cols = {"orderId","orderDate","customerId","customerName","orderTotal"};
        for (int i = 0; i < cols.length; i++) {
            tblOrders.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(cols[i]));
        }
        try {
            tblOrders.getItems().addAll(OrderDataAccess.findOrders(""));
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to load, try later!");
            navigateToHome(null);
        }
    }
    public void tblOrders_OnMouseClicked(MouseEvent mouseEvent) {
    }

    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        MainFormController.navigateToMain(root);
    }
}
