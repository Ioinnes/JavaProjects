package com.ioinnes.ru.springboot.testtask.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "image_URI")
    private String imageURI;
    @NotNull
    @Column(name = "username")
    private String userName;
    @NotNull
    @Column(name = "email")
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "status")
    private String status;

    public Timestamp getTimestampStatus() {
        return timestampStatus;
    }

    public void setTimestampStatus(Timestamp timestampStatus) {
        this.timestampStatus = timestampStatus;
    }

    @Column(name = "timestamp_status")
    private Timestamp timestampStatus;

    public User(String imageURI, String userName, String email, String mobileNumber, String status) {
        this.imageURI = imageURI;
        this.userName = userName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.status = status;
        this.timestampStatus = Timestamp.from(Instant.now());
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
