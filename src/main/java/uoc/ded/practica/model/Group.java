package uoc.ded.practica.model;

import java.time.LocalDate;

public class Group {
    private String groupId;
    private String description;
    private LocalDate date;
    private User[] members;

    public Group(String groupId, String description, LocalDate date, User[] members) {
        this.groupId = groupId;
        this.description = description;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User[] getMembers() {
        return members;
    }

    public void setMembers(User[] members) {
        this.members = members;
    }

    public boolean hasMembers() {
        if (this.members != null) {
            return this.members.length > 0;
        }
        return false;
    }

    public int numMembers() {
        if (this.members != null) {
            return this.members.length;
        }
        return 0;
    }
}
