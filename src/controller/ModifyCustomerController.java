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
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class controls the modify customer controller view where customers can be added, modified, and deleted*/
public class ModifyCustomerController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableColumn<Customer, String> customerAddressColumn;

    @FXML
    private TableColumn<Customer, String> customerCountryColumn;

    @FXML
    private ComboBox<String> customerCountryComboBox;

    @FXML
    private TableColumn<Customer, Integer> customerIDColumn;

    @FXML
    private TextField customerIDTextField;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private TableColumn<Customer, String> customerPhoneNumberColumn;

    @FXML
    private TextField customerPhoneNumberTextField;

    @FXML
    private TableColumn<Customer, String> customerPostalCodeColumn;

    @FXML
    private TextField customerPostalCodeTextField;

    @FXML
    private TableColumn<Customer, String> customerStateColumn;

    @FXML
    private ComboBox<String> customerStateComboBox;

    @FXML
    private TextField customerStreetAddressTextField;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    void deleteCustomerClick(ActionEvent event) throws SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All customer appointments will also be deleted. Are you sure you wish to delete the selected customer?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            int customerID = selectedCustomer.getCustomerID();
            String customerName = selectedCustomer.getCustomerName();
            CustomerQuery.deleteCustomer(customerID);
            CustomerQuery.deleteCustomerAppointments(customerID);
            customerTable.setItems(CustomerQuery.selectAllCustomers());
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, customerName + " has been successfully deleted");
            confirmation.show();
        }

    }


    @FXML
    void AddCustomerClick(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void ReturnToAppointmentsClick(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void countrySelect(ActionEvent event) throws SQLException {

        try {
            String selectedCountry = customerCountryComboBox.getValue();
            int countryID;

            if (selectedCountry.equals("U.S")) {
                countryID = 1;
                customerStateComboBox.setItems(CustomerQuery.selectedCountryStates(countryID));
            }

            if (selectedCountry.equals("UK")) {
                countryID = 2;
                customerStateComboBox.setItems(CustomerQuery.selectedCountryStates(countryID));
            }

            if (selectedCountry.equals("Canada")) {
                countryID = 3;
                customerStateComboBox.setItems(CustomerQuery.selectedCountryStates(countryID));
            }
        }
        catch(NullPointerException e){
            //empty
        }

    }

    @FXML
    void SaveCustomerClick(ActionEvent event) throws SQLException {

        try {
            int customerID = Integer.parseInt(customerIDTextField.getText());
            String customerName = customerNameTextField.getText();
            String customerAddress = customerStreetAddressTextField.getText();
            String customerPostalCode = customerPostalCodeTextField.getText();
            String phoneNumber = customerPhoneNumberTextField.getText();
            String division = customerStateComboBox.getValue();
            int divisionID = CustomerQuery.getDivisionID(division);

            if (customerName.isEmpty() || customerAddress.isEmpty() || customerPostalCode.isEmpty() || phoneNumber.isEmpty()) {
                Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION, "Please ensure all fields are completed");
                emptyAlert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to save the new customer information?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {

                    CustomerQuery.updateCustomer(customerAddress, customerPostalCode, divisionID, customerName, phoneNumber, customerID);
                    customerTable.setItems(CustomerQuery.selectAllCustomers());

                    Alert confirmation = new Alert(Alert.AlertType.INFORMATION, "Customer " + customerID + " has been successfully updated");
                    confirmation.show();
                }
            }
        }
        catch(NumberFormatException e){
            Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION, "Select the customer you wish to modify");
            emptyAlert.show();
        }
    }


    @FXML
    void getCustomerClick(ActionEvent event) throws SQLException {

        try {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            int customerID = selectedCustomer.getCustomerID();
            String customerName = selectedCustomer.getCustomerName();
            String customerAddress = selectedCustomer.getStreetAddress();
            String customerPostalCode = selectedCustomer.getPostalCode();
            String phoneNumber = selectedCustomer.getPhoneNumber();
            String country = selectedCustomer.getCountry();
            String state = selectedCustomer.getFirstLevelDivision();

            customerCountryComboBox.setItems(CustomerQuery.selectCountries());
            customerCountryComboBox.setValue(country);
            customerStateComboBox.setValue(state);

            customerIDTextField.setText(String.valueOf(customerID));
            customerNameTextField.setText(customerName);
            customerStreetAddressTextField.setText(customerAddress);
            customerPostalCodeTextField.setText(customerPostalCode);
            customerPhoneNumberTextField.setText(phoneNumber);

        }
        catch(NullPointerException e){

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select the customer you wish to modify");
             alert.show();
        }

    }

    /**Initializes the modifycustomercontroller view and populates the customer table with all customers*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            customerTable.setItems(CustomerQuery.selectAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        customerStateColumn.setCellValueFactory(new PropertyValueFactory<>("firstLevelDivision"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


    }


}

