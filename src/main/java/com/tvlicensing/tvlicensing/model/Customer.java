package com.tvlicensing.tvlicensing.model;

// @Entity tells JPA this class maps to a database table
import jakarta.persistence.Entity;
// @Table lets us specify the exact name of the database table
import jakarta.persistence.Table;
// @Id marks the field that is the primary key (unique identifier)
import jakarta.persistence.Id;
// @GeneratedValue tells the database to auto-generate the ID
// number automatically every time a new customer is saved
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
// @Column lets us configure individual database columns
import jakarta.persistence.Column;
// Imports for Bean Validation annotations
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// @Entity tells Spring Boot and JPA to create a database table based on this class
@Entity

public class Customer {

    // This is the primary key - a unique ID number for every customer
    // @GeneratedValue means the database assigns this number
    // automatically - we never set it manually
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The customer's full name
    // nullable = false means this column cannot be left empty
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message ="Name must be between 2 and 100 characters")
    @Pattern(regexp = "[A-Za-z]+ [A-Za-z]+.*", message = "Please enter your first and last name")
    @Column(nullable = false)
    private String fullName;

    // The customer's email address
    // unique = true means no two customers can share an email
    // nullable = false means it must always have a value
    // this is back end validation instead of the browser html required etc validation
    @NotBlank(message = "Email address is required")
    @Email(message = "Please enter a valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    // The customer's password
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false)
    private String password;

    // Address fields - matching what we built in the register form
    @NotBlank(message = "Address line 1 is required")
    @Column(nullable = false)
    private String addressLine1;

    // Address line 2 is optional - so nullable = true (the default)
    @Column
    private String addressLine2;

    @NotBlank(message = "Town or City is required")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "Postcode is required")
    @Column(nullable = false)
    private String postcode;
    // =============================================
    // CONSTRUCTORS
    // A constructor is a special method that creates
    // a new Customer object
    // =============================================

    // Empty constructor - required by JPA, do not remove this
    public Customer() {
    }

    // Full constructor - lets us create a Customer and fill
    // in all the details in one go
    public Customer(String fullName, String email, String password,
                    String addressLine1, String addressLine2,
                    String city, String postcode) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.postcode = postcode;
    }
    // =============================================
    // GETTERS AND SETTERS
    // These are methods that allow other classes to
    // read and update the fields of this class
    // Java convention is to never access fields directly
    // from outside a class - always use getters/setters
    // =============================================

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}