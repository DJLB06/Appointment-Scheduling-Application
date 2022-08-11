package model;
import java.time.LocalDate;
import java.time.LocalTime;


/** Creates the Appointment class with the expected variables, constructions, getters, and setters*/
public class Appointment {

    private int aptID;
    private String title;
    private String Description;
    private String Location;
    private String contact;
    private String Type;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int customerID;
    private int userID;

    public static int logOnChecker;

    public int getLogOnChecker() {
        return logOnChecker;
    }

    public void setLogOnChecker(int logOnChecker) {
        this.logOnChecker = logOnChecker;
    }

    public Appointment(int aptID, String title, String description, String location, String contact, String type, LocalDate date, LocalTime startTime, LocalTime endTime, int customerID, int userID) {
        this.aptID = aptID;
        this.title = title;
        this.Description = description;
        this.Location = location;
        this.contact = contact;
        this.Type = type;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerID = customerID;
        this.userID = userID;
    }

    /** Get the appointment ID*/
    public int getAptID() {
        return aptID;
    }

    /** Set the appointment ID*/
    public void setAptID(int aptID) {
        this.aptID = aptID;
    }

    /** Get the appointment title*/
    public String getTitle() {
        return title;
    }

    /** Set the appointment title*/
    public void setTitle(String title) {
        this.title = title;
    }

    /** Get the appointment description*/
    public String getDescription() {
        return Description;
    }

    /** Set the appointment description*/
    public void setDescription(String description) {
        this.Description = description;
    }

    /** Get the appointment location*/
    public String getLocation() {
        return Location;
    }

    /** Set the appointment location*/
    public void setLocation(String location) {
        this.Location = location;
    }

    /** Return the appointment contact*/
    public String getContact() {
        return contact;
    }

    /** Set the appointment contact*/
    public void setContact(String contact) {
        this.contact = contact;
    }

    /** Get the appointment type*/
    public String getType() {
        return Type;
    }

    /** Set the appointment type*/
    public void setType(String type) {
        this.Type = type;
    }

    /** Get the appointment date*/
    public LocalDate getDate() {
        return date;
    }

    /** Set the appointment date*/
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /** Get the appointment start time*/
    public LocalTime getStartTime() {
        return startTime;
    }

    /** Set the appointment start time*/
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /** Get the appointment end time*/
    public LocalTime getEndTime() {
        return endTime;
    }

    /** Set the appointment end time*/
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /** Get the appointment customer ID*/
    public int getCustomerID() {
        return customerID;
    }

    /** Set the appointment customer ID*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /** Get the appointment user ID*/
    public int getUserID() {
        return userID;
    }

    /** Set the appointment user ID*/
    public void setUserID(int userID) {
        this.userID = userID;
    }


}
