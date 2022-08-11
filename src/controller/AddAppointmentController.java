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
import model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

/**Class controls the add appointment controller view where appointments are added to the database*/
public class AddAppointmentController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField AppointmentIDField;

    @FXML
    private ComboBox<String> ContactComboBox;

    @FXML
    private ComboBox<Integer> CustomerIDComboBox;

    @FXML
    private DatePicker DateSelector;

    @FXML
    private TextField DescriptionField;

    @FXML
    private ComboBox<LocalTime> EndTimeComboBox;

    @FXML
    private TextField LocationField;

    @FXML
    private ComboBox<LocalTime> StartTimeComboBox;

    @FXML
    private TextField TitleField;

    @FXML
    private TextField TypeField;

    @FXML
    private ComboBox<Integer> UserIDComboBox;

    @FXML
    void BookAppointment(ActionEvent event) throws SQLException, IOException {

        try {
            String title = TitleField.getText();
            String description = DescriptionField.getText();
            String location = LocationField.getText();
            String type = TypeField.getText();
            int customerID = CustomerIDComboBox.getValue();
            int userID = UserIDComboBox.getValue();
            String contactName = ContactComboBox.getValue();
            int contactID = AppointmentsQuery.getContactID(contactName);

            LocalDate date = DateSelector.getValue();
            LocalTime startTime = StartTimeComboBox.getValue();
            LocalTime endTime = EndTimeComboBox.getValue();

            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
            startDateTime = User.convertToUTC(startDateTime);
            LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
            endDateTime = User.convertToUTC(endDateTime);

            Timestamp startTS = Timestamp.valueOf(startDateTime);
            Timestamp endTS = Timestamp.valueOf(endDateTime);
            
            if(startTime.isAfter(endTime)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Start time must be earlier than end time");
                alert.show();
            }

           else if(User.convertToEST(startDateTime, endDateTime)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment hours are between the hours of 08:00 AM and 22:00 PM EST");
                alert.show();
                }
           else if (User.overlappingAptCheck(startDateTime, endDateTime))
               {
                   Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment time is currently booked, please select an open time slot");
                   alert.show();
               }

            else {

                AppointmentsQuery.insertAppointment(title, description, location, type, customerID, userID, contactID, startTS, endTS);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch(NullPointerException e){

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please ensure all fields are filled out");
            alert.show();
        }

    }

    @FXML
    void CancelButton(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**Initializes the addappointmentcontroller view and sets the comboboxes with allowed and available values*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            UserIDComboBox.setItems(AppointmentsQuery.userIDList());
            CustomerIDComboBox.setItems(AppointmentsQuery.customerIDList());
            ContactComboBox.setItems(AppointmentsQuery.contactList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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


    }

}
