package controller;

import database.CustomerQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.Reports;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**Controls the reportsviewcontroller where user can view a number of reports based on type, month, country, or contact*/
public class ReportsViewController implements Initializable {

    Stage stage;
    Parent scene;


    @FXML
    private ComboBox<String> ContactDropBox;

    @FXML
    private TableColumn<Appointment, Integer> AptIDColumn;

    @FXML
    private ComboBox<String> CountryComboBox;

    @FXML
    private TableColumn<Customer, String> CountryCountryColumn;

    @FXML
    private TextField CountryField;

    @FXML
    private TableColumn<Customer, Integer> CountryIDColumn;

    @FXML
    private TableColumn<Customer, String> CountryNameColumn;

    @FXML
    private TableColumn<Customer, String> CountryPhoneColumn;

    @FXML
    private TableColumn<Customer, String> CountryStateColumn;

    @FXML
    private TableView<Customer> CountryTableView;

    @FXML
    private TableColumn<Appointment, Integer> CustomerIDColumn;

    @FXML
    private TableColumn<Appointment, LocalDate> DateColumn;

    @FXML
    private TableColumn<Appointment, String> DescriptionColumn;

    @FXML
    private TableColumn<Appointment, LocalTime> EndTimeColumn;

    @FXML
    private ComboBox<String> MonthComboBox;

    @FXML
    private TextField MonthField;

    @FXML
    private TableView<Appointment> ReportsTableView;

    @FXML
    private TableColumn<Appointment, LocalTime> StartTimeColumn;

    @FXML
    private TableColumn<Appointment, String> TitleColumn;

    @FXML
    private TableColumn<Appointment, String> TypeColumn;

    @FXML
    private ComboBox<String> TypeComboBox;

    @FXML
    private TextField TypeField;

    @FXML
    void ChangeCountryAction(ActionEvent event) throws SQLException {

        String country = CountryComboBox.getValue();
        int count = Reports.getCustomerCountryCount(country);
        CountryField.setText(String.valueOf(count));
        CountryTableView.setItems(Reports.getCustomerCountryList(country));
    }

    @FXML
    void ChangeMonthAction(ActionEvent event) throws SQLException {

        String month = MonthComboBox.getValue();
        int monthNumber = Reports.monthInteger(month);
        monthNumber = Reports.monthTotalAppointmentCount(monthNumber);
        MonthField.setText(String.valueOf(monthNumber));

    }

    @FXML
    void ChangeTypeAction(ActionEvent event) throws SQLException {

        String type =TypeComboBox.getValue();
        int count = Reports.getTypeCount(type);
        TypeField.setText(String.valueOf(count));

    }

    @FXML
    void ContactDropBoxAction(ActionEvent event) throws SQLException {

        String contactName = ContactDropBox.getValue();
        ReportsTableView.setItems(Reports.selectContactsAppointments(contactName));
    }

    @FXML
    void ExitReportButton(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }


/**Initializes the reports view controller. Populates the comboboxes with the appropriate information and sets table cell values*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ContactDropBox.setItems(Reports.selectContacts());
            TypeComboBox.setItems(Reports.selectTypes());
            CountryComboBox.setItems(CustomerQuery.selectCountries());
            MonthComboBox.setItems(Reports.monthList());
            }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        AptIDColumn.setCellValueFactory(new PropertyValueFactory<>("aptID"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        StartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        EndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        CountryIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        CountryNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        CountryCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        CountryStateColumn.setCellValueFactory(new PropertyValueFactory<>("firstLevelDivision"));
        CountryPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


    }
}
