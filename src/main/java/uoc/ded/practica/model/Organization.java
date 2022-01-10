package uoc.ded.practica.model;

import uoc.ei.tads.Iterador;
import uoc.ei.tads.Lista;
import uoc.ei.tads.ListaEncadenada;

public class Organization {
    private int organizationId;
    private String description;
    private  String name;
    private Lista<Activity> activities;

    public Organization(int organizationId, String name, String description) {
        this.organizationId = organizationId;
        this.name = name;
        this.description = description;
        activities = new ListaEncadenada<Activity>();
    }

    public String getName() {
        return name;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public String getDescription() {
        return description;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Iterador<Activity> activities() {
        return activities.elementos();
    }

    public void addActivity(Activity activity) {
        activities.insertarAlFinal(activity);
    }

    public int numActivities() {
        return activities.numElems();
    }

    public boolean hasActivities() {
        return activities.numElems() > 0;
    }
}
