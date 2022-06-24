package com.ioinnes.ru.springboot.testtask.entity;

import java.sql.Timestamp;
import java.util.Objects;

public class UserDTO {
    private int id;
    private String imageURI;
    private String userName;
    private String email;
    private String mobileNumber;
    private String status;
    private Timestamp timestampStatus;

    public UserDTO(String imageURI, String userName, String email, String mobileNumber) {
        this.imageURI = imageURI;
        this.userName = userName;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public int getId() {
        return id;
    }

    public UserDTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getImageURI() {
        return imageURI;
    }

    public UserDTO setImageURI(String imageURI) {
        this.imageURI = imageURI;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public UserDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public UserDTO setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public UserDTO setStatus(String status) {
        this.status = status;
        return this;
    }

    public Timestamp getTimestampStatus() {
        return timestampStatus;
    }

    public UserDTO setTimestampStatus(Timestamp timestampStatus) {
        this.timestampStatus = timestampStatus;
        return this;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", imageURI='" + imageURI + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", status='" + status + '\'' +
                ", timestampStatus=" + timestampStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return id == userDTO.id && imageURI.equals(userDTO.imageURI) && userName.equals(userDTO.userName) && email.equals(userDTO.email) && Objects.equals(mobileNumber, userDTO.mobileNumber) && status.equals(userDTO.status) && timestampStatus.equals(userDTO.timestampStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageURI, userName, email, mobileNumber, status, timestampStatus);
    }
}
