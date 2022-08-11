package controller;

import database.AppointmentsQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class provides the functionality for the user to modify appointment information */
public class ModifyAppointmentController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField AppointmentIDTextField;

    @FXML
    private Button BackToAppointmentView;

    @FXML
    private ComboBox<String> ContactComboBox;

    @FXML
    private ComboBox<Integer> CustomerIDComboBox;

    @FXML
    private DatePicker DateSelector;

    @FXML
    private TextField DescriptionTextField;

    @FXML
    private ComboBox<LocalTime> EndTimeComboBox;

    @FXML
    private TextField LocationTextField;

    @FXML
    private Button SaveModifications;

    @FXML
    private ComboBox<LocalTime> StartTimeComboBox;

    @FXML
    private TextField TitleTextField;

    @FXML
    private TextField TypeTextField;

    @FXML
    private ComboBox<Integer> userIDComboBox;

    @FXML
    void BackToAppointmentViewClick(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Any modifications that have not been saved will be lost. Do you want to continue?");
        Optional<ButtonType> result =  alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    @FXML
    void SaveModificationsClick(ActionEvent event) throws SQLException, IOException {

        int appointmentID = Integer.parseInt(AppointmentIDTextField.getText());
        String title = TitleTextField.getText();
        String description = DescriptionTextField.getText();
        String location = LocationTextField.getText();
        String contactName = ContactComboBox.getValue();
        int contactID = AppointmentsQuery.getContactID(contactName);
        String type = TypeTextField.getText();
        int customerID = CustomerIDComboBox.getValue();
        int userID = userIDComboBox.getValue();

        LocalDate date = DateSelector.getValue();
        LocalTime startTime = StartTimeComboBox.getValue();
        LocalTime endTime = EndTimeComboBox.getValue();

        LocalDateTime startDateTime = LocalDateTime.of(date,startTime);
        startDateTime = User.convertToUTC(startDateTime);
        LocalDateTime endDateTime = LocalDateTime.of(date,endTime);
        endDateTime = User.convertToUTC(endDateTime);

        Timestamp startTS = Timestamp.valueOf(startDateTime);
        Timestamp endTS = Timestamp.valueOf(endDateTime);

        if(title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() )
            {
                Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION, "Please ensure all fields are completed");
                emptyAlert.show();
            }
        else if(StartTimeComboBox.getValue().isAfter(EndTimeComboBox.getValue()))
            {
                Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION, "Start time must be earlier than end time");
                emptyAlert.show();
            }

        else if(User.convertToEST(startDateTime, endDateTime)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment hours are between the hours of 08:00 AM and 22:00 PM EST");
            alert.show();
        }

        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to save the new customer information?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                AppointmentsQuery.updateAppointment(appointmentID, title, description, location, type, customerID, userID, contactID, startTS, endTS);

                Alert confirmation = new Alert(Alert.AlertType.INFORMATION, "Appointment " + appointmentID + " has been successfully updated");
                confirmation.show();

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }

    }

    /** Initializes the modify appointment controller and populates the fields with the selected appointments data*/
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

            LocalTime startTime = LocalTime.of(0,0);
            LocalTime endTime = LocalTime.of(23,45);
            LocalTime manualInsert = LocalTime.of(23, 45);
            LocalTime manualInsert2 = LocalTime.of(23, 59);

            while(startTime.isBefore(endTime)){
                StartTimeComboBox.getItems().add(startTime);
                EndTimeComboBox.getItems().add(startTime);
                startTime = startTime.plusMinutes(15);
            }
            StartTimeComboBox.getItems().add(manualInsert);
            EndTimeComboBox.getItems().add(manualInsert);
            StartTimeComboBox.getItems().add(manualInsert2);
            EndTimeComboBox.getItems().add(manualInsert2);

        try
            {
            Appointment selectedAppointment = AppointmentViewController.getSelectedAppointment();

            int appointmentID = selectedAppointment.getAptID();
            String title = selectedAppointment.getTitle();
            String description = selectedAppointment.getDescription();
            String location = selectedAppointment.getLocation();
            String contact = selectedAppointment.getContact();
            String type = selectedAppointment.getType();
            int customerID = selectedAppointment.getCustomerID();
            int userID = selectedAppointment.getUserID();
            LocalDate date = selectedAppointment.getDate();
            LocalTime selectedStartTime = selectedAppointment.getStartTime();
            LocalTime selectedEndTime = selectedAppointment.getEndTime();

            DateSelector.setValue(date);
            StartTimeComboBox.setValue(selectedStartTime);
            EndTimeComboBox.setValue(selectedEndTime);
            ContactComboBox.setItems(AppointmentsQuery.contactList());
            ContactComboBox.setValue(contact);
            CustomerIDComboBox.setValue(customerID);
            CustomerIDComboBox.setItems(AppointmentsQuery.customerIDList());
            userIDComboBox.setValue(userID);
            userIDComboBox.setItems(AppointmentsQuery.userIDList());
            AppointmentIDTextField.setText(String.valueOf(appointmentID));
            TitleTextField.setText(title);
            DescriptionTextField.setText(description);
            LocationTextField.setText(location);
            TypeTextField.setText(type);
            }

        catch(NullPointerException | SQLException e)
            {
            //empty
            }

    }


}
