package com.ioinnes.ru.springboot.testtask.entity;


public class ActivityRequestDTO {
    private String status;
    private Integer id;


    public ActivityRequestDTO(String status, Integer id) {
        this.status = status;
        this.id = id;
    }

    @Override
    public String toString() {
        return "ActivityRequestDTO{" +
                "status='" + status + '\'' +
                ", id=" + id +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public ActivityRequestDTO setStatus(String status) {
        this.status = status;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public ActivityRequestDTO setId(int id) {
        this.id = id;
        return this;
    }
}