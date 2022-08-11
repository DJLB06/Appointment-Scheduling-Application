package model;

import javafx.collections.ObservableList;

/** Creates the customer class with the expected variables, constructions, getters, and setters*/
public class Customer {

    private String streetAddress;
    private String postalCode;
    private String country;
    private String firstLevelDivision;
    private String customerName;
    private String phoneNumber;
    private int customerID;


    public Customer(String streetAddress, String postalCode, String country, String firstLevelDivision, String customerName, String phoneNumber, int customerID) {
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.country = country;
        this.firstLevelDivision = firstLevelDivision;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.customerID = customerID;
    }





    /** Get the customer street address*/
    public String getStreetAddress() {
        return streetAddress;
    }

    /** Set the customer street address*/
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /** Get the customer postal code*/
    public String getPostalCode() {
        return postalCode;
    }

    /** Set the customer postal code*/
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    /** Get the customer country*/
    public String getCountry() {
        return country;
    }

    /** Set the customer country*/
    public void setCountry(String country) {
        this.country = country;
    }

    /** Get the customer State/Province*/
    public String getFirstLevelDivision() {
        return firstLevelDivision;
    }

    /** Set the customer State/Province*/
    public void setFirstLevelDivision(String firstLevelDivision) {
        this.firstLevelDivision = firstLevelDivision;
    }

    /** Get the customer name */
    public String getCustomerName() {
        return customerName;
    }

    /** Set the customer name */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Get the customer phone number*/
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /** Set the customer phone number*/
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /** Get the customer ID*/
    public int getCustomerID() {
        return customerID;
    }

    /** Set the customer ID*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;



    }
}
