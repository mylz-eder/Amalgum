package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.da.ProductDa;
import model.entity.product.Category;
import model.entity.product.Product;
import model.entity.product.Status;
import model.utils.Validation;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ProductController implements Initializable {
    private final Validation validation = new Validation();

    @FXML
    private TextField idTxt, productNameTxt, productCountTxt, productPriceTxt;

    @FXML
    private ComboBox<String> statusComBox, categoryComBox;


    @FXML
    private Button saveBtn, editBtn, deleteBtn, findBtn;

    @FXML
    private TableView<Product> productTbl;

    @FXML
    private TableColumn<Product, Integer> idCol, countCol, priceCol;

    @FXML
    private TableColumn<Product, String> productNameCol, statusCol, categoryCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (Status status : Status.values()) {
            statusComBox.getItems().add(status.name());
        }

        for (Category category : Category.values()) {
            categoryComBox.getItems().add(category.name());
        }


        resetForm();


        saveBtn.setOnAction(event -> {
            try (ProductDa productda = new ProductDa()) {
                Product product =
                        Product
                                .builder()
                                .name(validation.nameValidator(productNameTxt.getText()))
                                .status(Status.valueOf(statusComBox.getSelectionModel().getSelectedItem()))
                                .count(validation.posNumValidator(Integer.parseInt(productPriceTxt.getText())))
                                .price(validation.posNumValidator(Integer.parseInt(productCountTxt.getText())))
                                .category(Category.valueOf(categoryComBox.getSelectionModel().getSelectedItem()))
                                .build();
                productda.save(product);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product Saved\n" + product.toString());
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Product Save Error\n" + e.getMessage());
                alert.show();
            }
        });

        editBtn.setOnAction(event -> {
            try (ProductDa productDa = new ProductDa()) {
                // Data Validation
                Product product =
                        Product
                                .builder()
                                .id(Integer.parseInt(idTxt.getText()))
                                .name(validation.nameValidator(productNameTxt.getText()))
                                .status(Status.valueOf(statusComBox.getSelectionModel().getSelectedItem()))
                                .count(validation.posNumValidator(Integer.parseInt(productPriceTxt.getText())))
                                .price(validation.posNumValidator(Integer.parseInt(productCountTxt.getText())))
                                .category(Category.valueOf(categoryComBox.getSelectionModel().getSelectedItem()))
                                .build();
                productDa.edit(product);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product Edited\n" + product.toString());
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Product Edit Error\n" + e.getMessage());
                alert.show();
            }
        });

        deleteBtn.setOnAction(event -> {
            try (ProductDa productDa = new ProductDa()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure To Remove Product ? \n" +
                        "this Action Cannot be Taken Back!!!!");
                if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                    int id = Integer.parseInt(idTxt.getText());
                    productDa.remove(id);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product Removed With ID : " + id);
                    alert.show();
                    resetForm();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Product Remove Error\n" + e.getMessage());
                alert.show();
            }
        });

        productTbl.setOnMouseReleased(event-> {
            Product product = productTbl.getSelectionModel().getSelectedItem();
            idTxt.setText(String.valueOf(product.getId()));
            productNameTxt.setText(product.getName());
            statusComBox.getSelectionModel().select(product.getStatus().name());
            productPriceTxt.setText(String.valueOf(product.getCount()));
            productCountTxt.setText(String.valueOf(product.getPrice()));
            categoryComBox.getSelectionModel().select(product.getCategory().name());
        });
    }

    private void resetForm() {
        idTxt.clear();
        productNameTxt.clear();
        statusComBox.getSelectionModel().select(0);
        productPriceTxt.clear();
        productCountTxt.clear();
        categoryComBox.getSelectionModel().select(0);
        try (ProductDa productDa = new ProductDa()) {
            refreshTable(productDa.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Find Products Error\n" + e.getMessage());
            alert.show();
        }
    }

    private void refreshTable(List<Product> productList) {
        ObservableList<Product> products = FXCollections.observableList(productList);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("STATUS"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("PRODUCT_COUNT"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("PRODUCT_PRICE"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("PRODUCT_CATEGORY"));

        productTbl.setItems(products);
    }
}