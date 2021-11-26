package com.example.backend.viewModel;

public class HeartbeatModel {
    private Integer id;
    private String time;
    private Integer heartRate;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public Integer getHeartRate() {
        return heartRate;
    }
    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    
}
