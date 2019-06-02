package com.example.PETBook.Models;

public class ReportModel {

    private String emailUserReported;
    private String emailUserReporting;
    private String description;



    public String getEmailUserReported() {
        return emailUserReported;
    }

    public void setEmailUserReported(String emailUserReported) {
        this.emailUserReported = emailUserReported;
    }

    public String getEmailUserReporting() {
        return emailUserReporting;
    }

    public void setEmailUserReporting(String emailUserReporting) {
        this.emailUserReporting = emailUserReporting;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
