package controller;

import database.AppointmentsQuery;
import database.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.LogOnInterface;
import main.MonthIntegerInterface;
import model.Appointment;
import model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controls the main page where all appointments can be viewed or sorted. Provides paths to all other locations in the program*/
public class AppointmentViewController implements Initializable {


    Stage stage;
    Parent scene;



    @FXML
    private Button ExitProgram;

    @FXML
    private RadioButton AppointsByAllRbt;

    @FXML
    private RadioButton AppointsByMonthRbt;

    @FXML
    private RadioButton AppointsByWeekRbt;

    @FXML
    private TableView<Appointment> AppointmentsTable;

    @FXML
    private TableColumn<Appointment, Integer> AptIDColumn;

    @FXML
    private TableColumn<Appointment, String> ContactColumn;

    @FXML
    private TableColumn<Appointment, Integer> CustomerIDColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> DateColumn;

    @FXML
    private TableColumn<Appointment, String> DescriptionColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> EndTimeColumn;

    @FXML
    private TableColumn<Appointment, String> LocationColumn;


    @FXML
    private TableColumn<Appointment, LocalDateTime> StartTimeColumn;

    @FXML
    private TableColumn<Appointment, String> TitleColumn;

    @FXML
    private TableColumn<Appointment, String> TypeColumn;

    @FXML
    private TableColumn<Appointment, Integer> UserIDColumn;

    @FXML
    private ToggleGroup WeekMonth;


    @FXML
    void AddAppointmentButton(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }


    @FXML
    void AllAppointmentsRbt(ActionEvent event) {

        try {
            AppointmentsTable.setItems(AppointmentsQuery.selectAllAppointments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    @FXML
    void CancelAppointment(ActionEvent event) throws SQLException {

    try {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to cancel the selected appointment?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Appointment selectedAppointment = AppointmentsTable.getSelectionModel().getSelectedItem();
            int appointmentID = selectedAppointment.getAptID();
            String type = selectedAppointment.getType();
            AppointmentsQuery.deleteAppointment(appointmentID);
            Alert confirmation = new Alert(Alert.AlertType.INFORMATION, "Appointment " + appointmentID + " of appointment type " + type + " has been successfully cancelled.");
            confirmation.show();

            if(AppointsByAllRbt.isSelected()) {
                AppointmentsTable.setItems(AppointmentsQuery.selectAllAppointments());
            }
            else if(AppointsByMonthRbt.isSelected()){
                int month = Calendar.MONTH + 1;

                if(month == 13){
                    month = 1;
                }
                AppointmentsTable.setItems(AppointmentsQuery.selectMonthAppointments(month));
            }
            else if (AppointsByWeekRbt.isSelected()){
                LocalDateTime todaysDate = LocalDateTime.now();
                LocalDateTime todaysDateWeek = todaysDate.plusDays(7);
                Timestamp startTS = Timestamp.valueOf(todaysDate);
                Timestamp endTs = Timestamp.valueOf(todaysDateWeek);
                AppointmentsTable.setItems(AppointmentsQuery.selectWeekAppointments(startTS, endTs));
            }

        }
    }
    catch(NullPointerException e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select the appointment you wish to cancel");
        alert.show();
        }
    }

    @FXML
    void CustomerInformationButton(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ModifyCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void ExitProgramClick(ActionEvent event) {
        //  Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to exit the program?");
        //  Optional<ButtonType> result =  alert.showAndWait();

        //  if(result.isPresent() && result.get() == ButtonType.OK)
        //   {
        //      JDBC.closeConnection();
        //     System.exit(0);
        //  }
    }

    @FXML
    void ViewReportsClick(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportsView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    private static Appointment selectedAppointment;

    /** Passes the selected appointment to the modify appointment controller  */
    public static Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    @FXML
    void ModifyAppointmentButton(ActionEvent event) throws IOException {

        selectedAppointment = AppointmentsTable.getSelectionModel().getSelectedItem();

        if(selectedAppointment == null)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select the appointment you wish to modify");
                alert.show();
            }
        else
            {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/ModifyAppointment.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
    }

    @FXML
    void MonthAppointmentsRbt(ActionEvent event) {

        Calendar todaysDate = Calendar.getInstance();
        int month = todaysDate.get(Calendar.MONTH);

        MonthIntegerInterface addedMonth = monthIndex -> monthIndex + 1;
        month= addedMonth.month(month);

        if(month == 13){
            month = 1;
        }

        try {
            AppointmentsTable.setItems(AppointmentsQuery.selectMonthAppointments(month));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    @FXML
    void WeekAppointmentsRbt(ActionEvent event) {

        LocalDateTime todaysDate = LocalDateTime.now();
        LocalDateTime todaysDateWeek = todaysDate.plusDays(7);
        Timestamp startTS = Timestamp.valueOf(todaysDate);
        Timestamp endTs = Timestamp.valueOf(todaysDateWeek);

        try {
            AppointmentsTable.setItems(AppointmentsQuery.selectWeekAppointments(startTS, endTs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


    /** Initializes the appointment view form and controller. A lambda expression is used in the initialize method to set the action and alert for the exit program button. A Lambda expression is used in this method to ensure the alert for upcoming appointments only shows when initially logging on and not every time this controller is loaded. Another lambda expression is used to increment the integer of the month obtained by the calendar due to the calendar starting at index 0. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ExitProgram.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to exit the program?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                JDBC.closeConnection();
                System.exit(0);
            }
        });

        try {
            AppointmentsTable.setItems(AppointmentsQuery.selectAllAppointments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        AptIDColumn.setCellValueFactory(new PropertyValueFactory<>("aptID"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        ContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        StartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        EndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        UserIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));

        LocalTime logOnTime = LocalTime.now();



        if(Appointment.logOnChecker == 0) {
            try {
                ObservableList<Appointment> appointmentsList = User.startTimeAppointments(logOnTime);
                if (appointmentsList.isEmpty()) {
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "There are no upcoming appointments");
                    alert2.show();
                } else {
                    Appointment nearAppointment = appointmentsList.get(0);
                    String appointmentID = String.valueOf(nearAppointment.getAptID());
                    String appointmentDate = String.valueOf(nearAppointment.getDate());
                    String appointmentTime = String.valueOf(nearAppointment.getStartTime());

                    if (!User.startTimeAppointments(logOnTime).isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Upcoming appointment ID: " + appointmentID + " is scheduled for today " + appointmentDate + " at " + appointmentTime);
                        alert.show();
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        LogOnInterface logOnAttempts = logOnNumber -> logOnNumber + 1;
        Appointment.logOnChecker = logOnAttempts.logOnAdder(0);

    }
}
