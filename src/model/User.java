package model;

import database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;

/** Houses methods specific to the user information such as time conversions and login attempts*/
public class User {

    /** Check if username and password match */
    public static boolean checkUsername(String username, String password) throws SQLException {

        boolean check = false;
        String sql = "SELECT User_Name, Password FROM users WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {

            String usernameCheck = rs.getString("User_Name");
            String passwordCheck = rs.getString("Password");
            if ((usernameCheck.equals(username)) && (passwordCheck.equals(password)))
                {
                    check = true;
                }
            else
                {
                    check = false;
                }
            }
            return check;
    }

    /** Checks if the appointment being scheduled falls within EST business hours*/
    public static boolean convertToEST(LocalDateTime start, LocalDateTime end){

        ZoneId zoneID = ZoneId.of("US/Eastern");
        ZoneId zoneID2 = ZoneId.of("GMT");

        ZonedDateTime convertedTime = start.atZone(zoneID2);
        ZonedDateTime convertZDT = convertedTime.withZoneSameInstant(zoneID);
        LocalDateTime convertedLDT = convertZDT.toLocalDateTime();
        LocalTime convertedLocalTime = convertedLDT.toLocalTime();

        ZonedDateTime convertedTime2 = end.atZone(zoneID2);
        ZonedDateTime convertZDT2 = convertedTime2.withZoneSameInstant(zoneID);
        LocalDateTime convertedLDT2 = convertZDT2.toLocalDateTime();
        LocalTime convertedLocalTime2 = convertedLDT2.toLocalTime();

        LocalTime timeCheck = LocalTime.of(7,59);
        LocalTime timeCheck2 = LocalTime.of(22,01);

        if(convertedLocalTime.isAfter(timeCheck) && convertedLocalTime2.isBefore(timeCheck2) && convertedLocalTime2.isAfter(timeCheck))
            {
                return false;
            }

        else return true;

    }

    /** Checks if the new appointment being scheduled overlaps with any currently scheduled appointments*/
    public static boolean overlappingAptCheck(LocalDateTime start, LocalDateTime end) throws SQLException {


        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments, contacts WHERE appointments.Contact_ID = contacts.Contact_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
            {
                Timestamp startTime = rs.getTimestamp("Start");
                Timestamp endTime = rs.getTimestamp("End");

                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String contact = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                Timestamp dateTS = rs.getTimestamp("Start");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");

                LocalDate date = dateTS.toLocalDateTime().toLocalDate();
                LocalTime start1 = startTime.toLocalDateTime().toLocalTime();
                LocalTime end2 = endTime.toLocalDateTime().toLocalTime();


                LocalDateTime startT = startTime.toLocalDateTime();
                LocalDateTime endT = endTime.toLocalDateTime();

                if((start.isAfter(startT) && start.isBefore(endT)) || (start.isEqual(startT)) || end.isEqual(endT)|| (end.isAfter(startT) && end.isBefore(endT)) || (endT.isBefore(end) && startT.isAfter(start)) )
                {
                    Appointment appointment = new Appointment(appointmentID, title,description, location, contact, type, date, start1, end2, customerID, userID);
                    appointmentsList.add(appointment);
                }
            }

       if(appointmentsList.isEmpty()){
           return false;
       }
       else return true;

    }

    /** Converts local time to UTC*/
    public static LocalDateTime convertToUTC(LocalDateTime ldt){
        ZoneId zoneID = ZoneId.of("GMT");

        ZonedDateTime convertedTime = ldt.atZone(ZoneId.systemDefault());
        ZonedDateTime convertZDT = convertedTime.withZoneSameInstant(zoneID);
        LocalDateTime convertedLDT = convertZDT.toLocalDateTime();

        return convertedLDT;
    }

    /** Converts localdatetime to the users timezone*/
    public static LocalDateTime convertToLocalTime(LocalDateTime ldt){

        ZonedDateTime convertedTime = ldt.atZone(ZoneId.of("GMT"));

        ZonedDateTime convertZDT = convertedTime.withZoneSameInstant(ZoneId.systemDefault());

        LocalDateTime convertedLDT = convertZDT.toLocalDateTime();


        return convertedLDT;
    }

    public static ObservableList<Appointment> startTimeAppointments(LocalTime logOnTime) throws SQLException {
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

                long timeDifference = ChronoUnit.MINUTES.between(logOnTime, start);



                if(timeDifference <= 15 && timeDifference >= 0) {

                    Appointment appointment = new Appointment(appointmentID, title, description, location, contact, type, date, start, end, customerID, userID);
                    appointmentsList.add(appointment);
                }

            }

        return appointmentsList;
        }

    /** Records all login attempts */
        public static void loginAttempts (boolean login, String user, LocalDateTime loginTime) throws IOException {

        String fileName = "src/login_activity.txt";
        FileWriter fileWriter = new FileWriter(fileName, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        String attempt;

        if(login == true)
            {
                attempt = "User " + user + " successfully logged in at " + loginTime;
            }
        else
            {
                attempt = "User " + user + " had an unsuccessful attempt to log in at " + loginTime;
            }
            outputFile.println(attempt);
            outputFile.close();

        }


}
