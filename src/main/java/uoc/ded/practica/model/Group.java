package uoc.ded.practica.model;

import uoc.ei.tads.Iterador;
import uoc.ei.tads.ListaEncadenada;

import java.time.LocalDate;

public class Group {
    private String groupId;
    private String description;
    private LocalDate date;
    private ListaEncadenada<User> members;

    public Group(String groupId, String description, LocalDate date, ListaEncadenada<User> members) {
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

    public Iterador<User> members() {
        return this.members.elementos();
    }

    public void setMembers(ListaEncadenada<User> members) {
        this.members = members;
    }

    public boolean hasMembers() {
        if ( this.members == null ) {
            return false;
        }
        return this.members.numElems() > 0;
    }

    public int numMembers() {
        if (this.members != null) {
            return this.members.numElems();
        }
        return 0;
    }

    public Double valueOf() {
        Double badgeValueSum = 0.;

        for (Iterador<User> it = this.members(); it.haySiguiente();) {
            badgeValueSum += it.siguiente().getBadge(this.date).getValue();
        }
        Double averageBadgeValue = badgeValueSum / this.numMembers();
        return averageBadgeValue;
    }
}
