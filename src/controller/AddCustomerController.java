package controller;

import database.CustomerQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**Class controls the addcustomercontroller where the user can add customers to the database*/
public class AddCustomerController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField AddressField;

    @FXML
    private ComboBox<String> CountryComboBox;

    @FXML
    private TextField CustomerIDField;

    @FXML
    private TextField CustomerNameField;

    @FXML
    private TextField CustomerPhoneNumberField;

    @FXML
    private TextField PostalCodeField;

    @FXML
    private ComboBox<String> StateComboBox;

    @FXML
    void CancelButtonClick(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ModifyCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void countrySelect(ActionEvent event) throws SQLException {
        CountryComboBox.setItems(CustomerQuery.selectCountries());

        String selectedCountry = CountryComboBox.getValue();
        int countryID;

        if(selectedCountry.equals("U.S")){
            countryID = 1;
            StateComboBox.setItems(CustomerQuery.selectedCountryStates(countryID));}

        if (selectedCountry.equals("UK")){
            countryID = 2;
            StateComboBox.setItems(CustomerQuery.selectedCountryStates(countryID));}

        if(selectedCountry.equals("Canada")){
            countryID = 3;
            StateComboBox.setItems(CustomerQuery.selectedCountryStates(countryID));}
    }

    @FXML
    void SaveCustomerClick(ActionEvent event) throws IOException, SQLException {


        String customerName = CustomerNameField.getText();
        String customerAddress = AddressField.getText();
        String customerPostalCode = PostalCodeField.getText();
        String phoneNumber = CustomerPhoneNumberField.getText();
        String division = StateComboBox.getValue();

        if(customerName.isEmpty() || customerAddress.isEmpty() || customerPostalCode.isEmpty() || phoneNumber.isEmpty())
        {
            Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION, "Please ensure all fields are completed");
            emptyAlert.show();
        }

        else
            {
                int divisionID = CustomerQuery.getDivisionID(division);
                CustomerQuery.insertNewCustomer(customerAddress, customerPostalCode, divisionID, customerName, phoneNumber);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/ModifyCustomer.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

    }

    /**Initializes the add customer controller view and populates the country combo box with the available options*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            CountryComboBox.setItems(CustomerQuery.selectCountries());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}
