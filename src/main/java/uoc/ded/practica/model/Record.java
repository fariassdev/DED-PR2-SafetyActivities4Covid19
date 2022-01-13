package uoc.ded.practica.model;

import uoc.ded.practica.SafetyActivities4Covid19;

import java.util.Date;

public class Record {

    private String actId;
    private String description;
    private String recordId;
    private Date dateAct;
    private Date dateStatus;
    private String descriptionStatus;
    private SafetyActivities4Covid19.Mode mode;
    private int num;
    private SafetyActivities4Covid19.Status status;
    private Organization organization;


    public Record(String recordId, String actId, String description, Date date,
                  SafetyActivities4Covid19.Mode mode, int num, Organization organization) {
        this.recordId = recordId;
        this.actId = actId;
        this.description = description;
        this.dateAct = date;
        this.mode = mode;
        this.num = num;
        this.status = SafetyActivities4Covid19.Status.PENDING;
        this.organization = organization;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionStatus() {
        return descriptionStatus;
    }

    public void setDescriptionStatus(String descriptionStatus) {
        this.descriptionStatus = descriptionStatus;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Date getDateAct() {
        return dateAct;
    }

    public Date getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(Date dateStatus) {
        this.dateStatus = dateStatus;
    }

    public void setDateAct(Date date) {
        this.dateAct = date;
    }

    public SafetyActivities4Covid19.Mode getMode() {
        return mode;
    }

    public void setMode(SafetyActivities4Covid19.Mode mode) {
        this.mode = mode;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public SafetyActivities4Covid19.Status getStatus() {
        return status;
    }

    public void setStatus(SafetyActivities4Covid19.Status status) {
        this.status = status;
    }

    public void update(SafetyActivities4Covid19.Status status, Date date, String description) {
        this.setStatus(status);
        this.setDateStatus(date);
        this.setDescriptionStatus(description);
    }

    public boolean isEnabled() {
        return this.status == SafetyActivities4Covid19.Status.ENABLED;
    }

    public Activity newActivity() {
        Activity activity = new Activity(this.actId, this.description, this.dateAct, this.mode, this.num, this);
        this.organization.addActivity(activity);

        return activity;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
