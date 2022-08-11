package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.User;

import java.sql.*;
import java.time.*;


/**Creates the appointments query class to house all the necessary functions when querying the database in relation to appointments*/
public abstract class AppointmentsQuery {

    /** inserts an appointment into the database */
    public static int insertAppointment(String title, String description, String location, String type, int customerID, int userID, int contactID, Timestamp start, Timestamp end) throws SQLException {
        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Customer_ID, User_ID, Contact_ID, Start, End) VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setInt(5, customerID);
        ps.setInt(6, userID);
        ps.setInt(7, contactID);
        ps.setTimestamp(8, start);
        ps.setTimestamp(9, end);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }


    /** updates an appointment in the database*/
        public static int updateAppointment(int appointmentID, String title, String description, String location, String type, int customerID, int userID, int contactID, Timestamp start, Timestamp end) throws SQLException {

        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?, Start = ?, End = ?  WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setInt(5, customerID);
            ps.setInt(6, userID);
            ps.setInt(7, contactID);
            ps.setTimestamp(8,start);
            ps.setTimestamp(9,end);
            ps.setInt(10, appointmentID);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        }

    /** Get Contact ID based off Contact Name */
    public static int getContactID(String contact) throws SQLException {

        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contact);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int contactID = rs.getInt("Contact_ID");
        return contactID;
    }

        /** Get list of all user IDs*/
    public static ObservableList<Integer> userIDList() throws SQLException {

        ObservableList<Integer> userIDList = FXCollections.observableArrayList();
        String sql = "SELECT User_ID FROM users";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            int userID = rs.getInt("User_ID");
            userIDList.add(userID);
        }
        return userIDList;
    }

    /** Get list of all Customer IDs*/
    public static ObservableList<Integer> customerIDList() throws SQLException {

        ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
        String sql = "SELECT Customer_ID FROM customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            int customerID = rs.getInt("Customer_ID");
            customerIDList.add(customerID);
        }
        return customerIDList;
    }

    /** Get list of all Contacts */
    public static ObservableList<String> contactList() throws SQLException {

        ObservableList<String> contactList = FXCollections.observableArrayList();
        String sql = "SELECT Contact_Name FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            String contact = rs.getString("Contact_Name");
            contactList.add(contact);
        }
        return contactList;
    }



    /** deletes an appointment from the database */
        public static int deleteAppointment(int appointmentID) throws SQLException {

        String sql = "DELETE FROM appointments WHERE Appointment_ID = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
        }



        /** Selects all information from appointments */
        public static ObservableList<Appointment> selectAllAppointments() throws SQLException {

            ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM appointments, contacts WHERE appointments.Contact_ID = contacts.Contact_ID ORDER BY Appointment_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
                while(rs.next())
                {
                    int appointmentID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String contact = rs.getString("Contact_Name");
                    String type = rs.getString("Type");
                    Date dateTS = rs.getDate("Start");
                    Time startTime = rs.getTime("Start");
                    Time endTime = rs.getTime("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");

                    LocalDate date = dateTS.toLocalDate();
                    LocalTime start = startTime.toLocalTime();
                    LocalTime end = endTime.toLocalTime();

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

    /** Selects all information from an appointment by current month*/
    public static ObservableList<Appointment> selectMonthAppointments(int month) throws SQLException {

        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments, contacts WHERE appointments.Contact_ID = contacts.Contact_ID AND Month(Start) = ? ORDER BY Appointment_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, month);
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

    /** Selects all information from an appointment by todays date + 7 days*/
    public static ObservableList<Appointment> selectWeekAppointments(Timestamp startTS, Timestamp endTS) throws SQLException {

        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments, contacts WHERE appointments.Contact_ID = contacts.Contact_ID AND Start >= ? and End <= ? ORDER BY Appointment_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setTimestamp(1, startTS);
        ps.setTimestamp(2, endTS);
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


    /** Selects appointment by appointment ID */
    public static void selectAppointment(int appointmentID) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int appointmentID1 = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Location");
            String type = rs.getString("Type");
            String start = rs.getString("Start");
            String end = rs.getString("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
        }
    }



}
