package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.da.PersonDa;
import model.entity.person.Occupation;
import model.entity.person.Person;
import model.utils.Validation;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PersonController implements Initializable {

    private final Validation validation = new Validation();

    @FXML
    private TextField idTxt, nameTxt, lastNameTxt, ageTxt;

    @FXML
    private ComboBox<String> occupationComBox;

    @FXML
    private DatePicker birthDateDatePicker;

    @FXML
    private RadioButton entryRdo, middleGradeRdo, expertRdo;

    @FXML
    private Button saveBtn, editBtn, deleteBtn, findBtn;

    @FXML
    private TableView<Person> personTbl;

    @FXML
    private TableColumn <Person, Integer> idCol, ageCol;

    @FXML
    private TableColumn <Person, String> firstNameCol, lastNameCol, occupationCol, experienceCol;

    @FXML
    private TableColumn <Person, LocalDate> birthDateCol;

    private Person person;

    public void initialize(URL location, ResourceBundle resources) {
        for (Occupation occupation : Occupation.values()) {
            occupationComBox.getItems().add(occupation.name());
        }

        //performs a basic form reset

        resetForm();


        ToggleGroup experienceGroup = new ToggleGroup();
        entryRdo.setToggleGroup(experienceGroup);
        middleGradeRdo.setToggleGroup(experienceGroup);
        expertRdo.setToggleGroup(experienceGroup);

        person = new Person();

        // this part updates the information based on the pre-active info on the form. in our program
        // this is redundant, but it is good to have this as a benchmark for the future

        if (entryRdo.isSelected()) {
            person.setEntryExp(true);

        } else if (middleGradeRdo.isSelected()) {
            person.setMiddleExp(true);

        } else if (expertRdo.isSelected()) {
            person.setExpertExp(true);
        }

        // this part should alter the value of the three Boolean information based on alterations
        // within the fxml display

        experienceGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == entryRdo) {
                person.setEntryExp(true);

            } else if (newValue == middleGradeRdo) {
                person.setMiddleExp(true);

            } else if (newValue == expertRdo) {
                person.setExpertExp(true);
            }
        });

        saveBtn.setOnAction(event -> {
                    try (PersonDa personDa = new PersonDa()) {
                        Person person =
                                Person
                                        .builder()
                                        .firstName(validation.nameValidator(nameTxt.getText()))
                                        .lastName(validation.nameValidator((lastNameTxt.getText())))
                                        .age(validation.posNumValidator(Integer.parseInt(ageTxt.getText())))
                                        .occupation(Occupation.valueOf(occupationComBox.getSelectionModel().getSelectedItem()))
                                        .birthDate(birthDateDatePicker.getValue())
                                        .entryExp(entryRdo.isSelected())
                                        .middleExp(middleGradeRdo.isSelected())
                                        .expertExp(expertRdo.isSelected())
                                        .build();
                        personDa.save(person);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Person Saved \n" + person.toString());
                        alert.show();
                        resetForm();

                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Person Save Error \n" + e.getMessage());
                        alert.show();
                    }
        });

//todo fix the Edit Button
        editBtn.setOnAction(event -> {
            try (PersonDa personDa = new PersonDa()) {
                Person person =
                        Person
                                .builder()
                                .id(Integer.parseInt(idTxt.getText()))
                                .firstName(validation.nameValidator(nameTxt.getText()))
                                .lastName(validation.nameValidator((lastNameTxt.getText())))
                                .age(validation.posNumValidator(Integer.parseInt(ageTxt.getText())))
                                .occupation(Occupation.valueOf(occupationComBox.getSelectionModel().getSelectedItem()))
                                .birthDate(birthDateDatePicker.getValue())

                                .entryExp(entryRdo.isSelected())
                                .middleExp(middleGradeRdo.isSelected())
                                .expertExp(expertRdo.isSelected())
                                .build();
                personDa.edit(person);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Person Edited \n" + person.toString());
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Person Edit Error \n" + e.getMessage());
                alert.show();
            }
        });

    deleteBtn.setOnAction(event -> {
        try (PersonDa personDa = new PersonDa()) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you Sure About That? \n" + "this decision cannot be taken back!!!");
            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                int id = Integer.parseInt(idTxt.getText());
                personDa.delete(id);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Person Record With ID: " + id + " Removed");
                alert.show();
                resetForm();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Person Record Remove Error\n" + e.getMessage());
            alert.show();
        }
    });





        personTbl.setOnMouseReleased(event -> {
            Person person = personTbl.getSelectionModel().getSelectedItem();
            idTxt.setText(String.valueOf(person.getId()));
            nameTxt.setText(person.getFirstName());
            lastNameTxt.setText(person.getLastName());
            ageTxt.setText(String.valueOf(person.getAge()));
            occupationComBox.getSelectionModel().select(String.valueOf(person.getOccupation()));

            if (person.getBirthDate() != null) {
                LocalDate localDate = person.getBirthDate();
                birthDateDatePicker.setValue(localDate);
            }
            // todo figure out a way to actually display these, Possibly by ruining the already made structure

//            entryRdo.getTypeSelector(isEntryExp());
//            middleGradeRdo.setSelected(person.isMiddleExp());
//            expertRdo.setSelected(person.isExpertExp());


        });
}

        // creates the resetForm Function
        private void resetForm () {
            idTxt.clear();
            nameTxt.clear();
            lastNameTxt.clear();
            ageTxt.clear();
            occupationComBox.getSelectionModel().select(0);
            birthDateDatePicker.setValue(LocalDate.now());
            entryRdo.setSelected(false);
            middleGradeRdo.setSelected(false);
            expertRdo.setSelected(false);
            try (PersonDa personDa = new PersonDa()) {
                refreshTable (personDa.findAll());
            } catch (Exception e) {
                Alert alert = new Alert (Alert.AlertType.ERROR, "Reset Form Error \n" + e.getMessage());
                alert.show();
            }
        }

        private void refreshTable (List<Person> personList) {
            ObservableList<Person> people = FXCollections.observableList(personList);

            idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("NAME"));
            lastNameCol.setCellValueFactory(new PropertyValueFactory<>("LAST_NAME"));
            ageCol.setCellValueFactory(new PropertyValueFactory<>("AGE"));
            occupationCol.setCellValueFactory(new PropertyValueFactory<>("OCCUPATION"));
            birthDateCol.setCellValueFactory(new PropertyValueFactory<>("BIRTHDATE"));
            experienceCol.setCellValueFactory(new PropertyValueFactory<>("EXPERIENCE"));

            personTbl.setItems(people);

        }
    }
