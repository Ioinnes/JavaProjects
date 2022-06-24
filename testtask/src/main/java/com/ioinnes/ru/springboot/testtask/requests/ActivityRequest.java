package com.ioinnes.ru.springboot.testtask.requests;


public class ActivityRequest {
    private Integer id;
    private String status;


    public ActivityRequest(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ActivityRequest{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
