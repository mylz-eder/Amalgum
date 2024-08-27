package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.da.SimCardDa;
import model.entity.simcard.Provider;
import model.entity.simcard.SimCard;
import model.utils.Validation;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class SimCardController implements Initializable {
    private final Validation validation = new Validation();

    @FXML
    private TextField idTxt, profileNameTxt, phoneNumberTxt, balanceTxt;

    @FXML
    private ComboBox<String> providerComBox;

    @FXML
    private RadioButton monthlyRdo, manualRdo;

    @FXML
    private Button saveBtn, editBtn, deleteBtn, findBtn;

    @FXML
    private DatePicker signupDatePicker;

    @FXML
    private TableView<SimCard> simCardTbl;

    @FXML
    private TableColumn<SimCard, Integer> userIdCol, lastInvoiceCol, balanceCol;

    @FXML
    private TableColumn <SimCard, String> nameCol, phoneNumberCol;

    @FXML
    private TableColumn <SimCard, LocalDate> singDateCol;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Provider provider : Provider.values()) {
            providerComBox.getItems().add(provider.name());
        }

        resetForm();


        saveBtn.setOnAction(event -> {
            try (SimCardDa simCardDa = new SimCardDa()) {
                SimCard simCard =
                        SimCard
                                .builder()
                                .name(validation.nameValidator(profileNameTxt.getText()))
                                .phoneNumber(validation.phoneNumValidator(phoneNumberTxt.getText()))
                                .provider(Provider.valueOf(String.valueOf(providerComBox.getSelectionModel().getSelectedItem())))
                                .paymentPlan(simCard.getPaymentPlan())
                                .purchaseDate(purchaseDateDisplay.getValue())
                                .build();
                simcardDa.save(simCard);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Simcard Saved!\n" + simCard.toString());
                alert.show();
                resetForm();

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Product Save Error\n" + e.getMessage());
                alert.show();
            }
        });

        editBtn.setOnAction(event -> {
            try (SimcardDa simcardDa = new SimcardDa()) {
                Simcard simcard =
                        Simcard
                                .builder()
                                .id(Integer.parseInt(idTxt.getText()))
                                .name(validation.nameValidator(nameTxt.getText()))
                                .phoneNumber(validation.longvalidation(Long.parseLong(phoneNumberTxt.getText())))
                                .plan(String.valueOf(BillingPlans.valueOf(billingPlanComBox.getSelectionModel().getSelectedItem())))
                                .company(String.valueOf(CompanyNames.valueOf(companyComBox.getSelectionModel().getSelectedItem())))
                                .purchaseDate(purchaseDateDisplay.getValue())
                                .build();
                simcardDa.edit(simcard);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Simcard Edited\n" + simcard.toString());
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Simcard Edit Error\n" + e.getMessage());
                alert.show();
            }
        });

        deleteBtn.setOnAction(event -> {
            try (SimcardDa simcardDa = new SimcardDa()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you CERTAIN you want to Delete this Simcard? \n" +
                        "this action cannot be taken back!!!");
                if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                    int id = Integer.parseInt(idTxt.getText());
                    simcardDa.delete(id);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Simcard Removed With ID : " + id);
                    alert.show();
                    resetForm();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Product Remove Error\n" + e.getMessage());
                alert.show();
            }
        });

        simTbl.setOnMouseReleased(event -> {
            Simcard simcard = simTbl.getSelectionModel().getSelectedItem();
            idTxt.setText(String.valueOf(simcard.getId()));
            nameTxt.setText(simcard.getName());
            phoneNumberTxt.setText(String.valueOf(simcard.getPhoneNumber()));
            billingPlanComBox.getSelectionModel().select(simcard.getPlan());
            companyComBox.getSelectionModel().select(simcard.getCompany());
//            purchaseDateDisplay.setValue(simcard.setPurchaseDate());
        });
    }

    private void resetForm() {
        idTxt.clear();
        nameTxt.clear();
        phoneNumberTxt.clear();
        billingPlanComBox.getSelectionModel().select(0);
        companyComBox.getSelectionModel().select(0);
        purchaseDateDisplay.getChronology();
        try (SimcardDa simcardDa = new SimcardDa()) {
            refreshTable(simcardDa.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Find Simcard Error\n" + e.getMessage());
            alert.show();
        }
    }

    public void refreshTable(List<Simcard> simcardList) {
        ObservableList<Simcard> simcards = FXCollections.observableList(simcardList);

        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("NAME"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("PHONE_NUMBER"));
        planCol.setCellValueFactory(new PropertyValueFactory<>("PLAN"));
        companyCol.setCellValueFactory(new PropertyValueFactory<>("COMPANY"));
        purchaseDateCol.setCellValueFactory(new PropertyValueFactory<>("PURCHASE_DATE"));

        simTbl.setItems(simcards);
    }
}
