package model;

import database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**Class houses the methods used for reports*/
public abstract class  Reports {


    /** Selects all information appointments*/
    public static ObservableList<Appointment> selectContactsAppointments(String contactName) throws SQLException {

        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments, contacts WHERE appointments.Contact_ID = contacts.Contact_ID AND Contact_Name = ? ORDER BY Appointment_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contactName);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String contact = rs.getString("Contact_Name");
            String type = rs.getString("Type");
            Timestamp dateTS = rs.getTimestamp("Start");
            Timestamp startTime = rs.getTimestamp("Start");
            Timestamp endTime = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            LocalDate date = dateTS.toLocalDateTime().toLocalDate();
            LocalTime start = startTime.toLocalDateTime().toLocalTime();
            LocalTime end = endTime.toLocalDateTime().toLocalTime();

            LocalDateTime dateLDT = LocalDateTime.of(date, start);
            LocalDateTime startLDT = LocalDateTime.of(date,start);
            LocalDateTime endLDT = LocalDateTime.of(date,end);

            dateLDT = User.convertToLocalTime(dateLDT);
            date = dateLDT.toLocalDate();

            startLDT = User.convertToLocalTime(startLDT);
            start = startLDT.toLocalTime();

            endLDT = User.convertToLocalTime(endLDT);
            end = endLDT.toLocalTime();


            Appointment appointment = new Appointment(appointmentID, title,description, location, contact, type, date, start, end, customerID, userID);
            appointmentsList.add(appointment);

        }
        return appointmentsList;
    }

    /** Returns a list of all contacts in database */
    public static ObservableList<String> selectContacts() throws SQLException {

        ObservableList<String> contactList = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT Contact_Name FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            String contacts = rs.getString("Contact_name");
            contactList.add(contacts);
        }
        return contactList;
    }


    /** Returns a list of all Types in database */
    public static ObservableList<String> selectTypes() throws SQLException {

        ObservableList<String> typeList = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT Type FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            String type = rs.getString("Type");
            typeList.add(type);
        }
        return typeList;
    }

    /** Returns a list of the month names */
    public static ObservableList<String> monthList() {

        ObservableList<String> monthList = FXCollections.observableArrayList();
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");
        return monthList;
    }

    /** Returns an integer based on the month name */
    public static int monthInteger(String month) {

        int monthInteger = 0;
        if(month.equals("January"))
            {
                monthInteger = 1;
            }
        else if(month.equals("February"))
        {
            monthInteger = 2;
        }
        else if(month.equals("March"))
        {
            monthInteger = 3;
        }
        else if(month.equals("April"))
        {
            monthInteger = 4;
        }
        else if(month.equals("May"))
        {
            monthInteger = 5;
        }
        else if(month.equals("June"))
        {
            monthInteger = 6;
        }
        else if(month.equals("July"))
        {
            monthInteger = 7;
        }
        else if(month.equals("August"))
        {
            monthInteger = 8;
        }
        else  if(month.equals("September"))
        {
            monthInteger = 9;
        }
        else if(month.equals("October"))
        {
            monthInteger = 10;
        }
        else if(month.equals("November"))
        {
            monthInteger = 11;
        }
       else if(month.equals("December"))
        {
            monthInteger = 12;
        }

        return monthInteger;

    }


    /** Returns the number of appointments in the selected month */
    public static int monthTotalAppointmentCount(int month) throws SQLException {

        String sql = "SELECT COUNT(Month(Start)) AS total FROM appointments WHERE Month(Start) = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, month);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int total = rs.getInt("total");
        return total;
    }

    /** Get number of appointments by type */
    public static int getTypeCount(String type) throws SQLException {

        String sql = "SELECT COUNT(Type) AS total FROM appointments WHERE Type = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, type);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int total = rs.getInt("total");
        return total;
    }

    /** Get number of appointments by country */
    public static int getCustomerCountryCount(String country) throws SQLException {

        String sql = "SELECT COUNT(Country) AS total FROM customers, countries, first_level_divisions WHERE customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.Country_ID = countries.Country_ID AND Country = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int total = rs.getInt("total");
        return total;
    }


    /** Get list of all customer in a country */
    public static ObservableList<Customer> getCustomerCountryList(String country) throws SQLException {

        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers, countries, first_level_divisions WHERE customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.Country_ID = countries.Country_ID AND Country = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String streetAddress = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phoneNumber = rs.getString("Phone");
            String stateProvince = rs.getString("Division");
            String country2 = rs.getString("Country");

            Customer customer = new Customer(streetAddress, postalCode, country2, stateProvince, customerName, phoneNumber, customerID);
            customerList.add(customer);

        }
        return customerList;

    }



}
