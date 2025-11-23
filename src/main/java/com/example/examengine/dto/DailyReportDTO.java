package com.example.examengine.dto;

public class DailyReportDTO {

    private String candidateName;
    private Integer registrationNumber;
    private String appliedPost;
    private String centreName;
    private String slotTime;
    private String examDate;

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public Integer getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(Integer registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getAppliedPost() {
        return appliedPost;
    }

    public void setAppliedPost(String appliedPost) {
        this.appliedPost = appliedPost;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }
}
