package com.example.dsr.repository;

public interface AttendanceProjection {

    Long getAttendanceId();
    String getAttendanceDate();
    String getPunchTime();
    String getRemarks();
    String getShift();
    String getWorkedHrs();
    String getStatus();
    String getAlltime();
    String getIntime();
}
