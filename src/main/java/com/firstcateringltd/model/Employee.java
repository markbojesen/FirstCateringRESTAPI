package com.firstcateringltd.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "employee")
public class Employee extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(name = "mobile_number", nullable = false)
    private String mobile;

    @Column(name = "credit", nullable = false)
    private Double credit;

    @Column(name = "email_address", nullable = false)
    private String email;

    @Column(name = "card_registered", nullable = false)
    private Boolean cardRegistered;

    @Column(name = "data_card")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Only alphanumeric characters allowed")
    private String dataCard;

    @Column(name = "welcome_message")
    private String welcomeMessage;

    public Employee(String mobile, Double credit, String email, Boolean cardRegistered, String dataCard, String firstName, String lastName, String creditCard) {
        super(firstName, lastName, creditCard);
        this.mobile = mobile;
        this.credit = credit;
        this.email = email;
        this.cardRegistered = cardRegistered;
        this.dataCard = dataCard;
    }

    // Mandatory default constructor
    public Employee() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isCardRegistered() {
        return cardRegistered;
    }

    public void setCardRegistered(Boolean cardRegistered) {
        this.cardRegistered = cardRegistered;
    }

    public Double addCredit(Double credit) {
        return this.credit =+ credit;
    }

    public String getDataCard() {
        return dataCard;
    }

    public void setDataCard(String dataCard) {
        this.dataCard = dataCard;
    }

    public Boolean getCardRegistered() {
        return cardRegistered;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public void doubleTapped() {
        System.out.println("Double tapped");
    }

    @Override
    public String toString() {
        return "Employee id = " + employeeId +
                ", first name = " + firstName +
                ", last name = " + lastName +
                ", amount of credit = " + credit +
                ", card registered = " + cardRegistered;
    }
}