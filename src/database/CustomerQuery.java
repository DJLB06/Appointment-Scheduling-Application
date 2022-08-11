package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/** This class contains the methods for the communication between the program and database for customer information */
public abstract class CustomerQuery {



    /** Adds a customer to the database */
    public static int insertNewCustomer(String streetAddress, String postalCode, int divisionID, String customerName, String phoneNumber) throws SQLException {

        String sql = "INSERT INTO customers (Address, Postal_Code, Division_ID, Customer_Name, Phone,  Customer_ID) VALUES(?, ?, ?, ?, ?, NULL)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, streetAddress);
        ps.setString(2, postalCode);
        ps.setInt(3, divisionID);
        ps.setString(4, customerName);
        ps.setString(5, phoneNumber);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /** Updates a customer in the database */
    public static int updateCustomer(String streetAddress, String postalCode, int divisionID, String customerName, String phoneNumber, int customerID) throws SQLException {

        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, streetAddress);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNumber);
        ps.setInt(5, divisionID);
        ps.setInt(6, customerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

    /** Returns a list of all countries in database */
    public static ObservableList<String> selectCountries() throws SQLException {

        ObservableList<String> customerList = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT Country FROM countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            String country = rs.getString("Country");
            customerList.add(country);
        }
        return customerList;
    }


    /** Get a list of states based on the country selection */
    public static ObservableList<String> selectedCountryStates(int countryID) throws SQLException {

        ObservableList<String> selectedCountryStates = FXCollections.observableArrayList();
        String sql = "SELECT Division FROM first_level_divisions WHERE Country_ID = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryID);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            String state = rs.getString("Division");
            selectedCountryStates.add(state);
        }
        return selectedCountryStates;

    }


    /** Get Division ID based off Division */
    public static int getDivisionID(String division) throws SQLException {

        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, division);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int divisionID = rs.getInt("Division_ID");
        return divisionID;
    }


    /** Returns a list of all customers in database */
    public static ObservableList<Customer> selectAllCustomers() throws SQLException {

        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers, first_level_divisions, countries WHERE customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.Country_ID = countries.Country_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            int customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String streetAddress = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phoneNumber = rs.getString("Phone");
            String stateProvince = rs.getString("Division");
            String country = rs.getString("Country");

            Customer customer = new Customer(streetAddress, postalCode, country, stateProvince, customerName, phoneNumber, customerID);
            customerList.add(customer);
        }
        return customerList;
    }


    /** deletes customer from the database */
    public static int deleteCustomer(int customerID) throws SQLException {

        String sql = "DELETE FROM customers WHERE Customer_ID = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /** deletes all appointments for a specified customer from the database */
    public static int deleteCustomerAppointments(int customerID) throws SQLException {

        String sql = "DELETE FROM appointments WHERE Customer_ID = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }




}
