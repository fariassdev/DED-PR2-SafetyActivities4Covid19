package uoc.ded.practica.model;

import uoc.ded.practica.SafetyActivities4Covid19;

import java.util.Date;

public class Group {
    private String groupId;
    private String description;
    private Date creationDate;
    private String[] members;

    public Group(String groupId, String description, Date creationDate, String[] members) {
        this.groupId = groupId;
        this.description = description;
        this.creationDate = creationDate;
        this.members = members;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }
}
