package uoc.ded.practica.model;

import uoc.ei.tads.Iterador;
import uoc.ei.tads.Lista;
import uoc.ei.tads.ListaEncadenada;

import java.util.Comparator;

public class Organization {
    public static final Comparator<Organization> CMP_V = (Organization o1, Organization o2)->Double.compare(o1.getAverageActivityRating(), o2.getAverageActivityRating());

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

    public Double getAverageActivityRating() {
        Double activityRatingSum = 0.;
        Iterador<Activity> it = this.activities.elementos();
        while (it.haySiguiente()) {
            activityRatingSum = activityRatingSum + it.siguiente().rating();
        }
        Double activityRatingAverage = activityRatingSum / this.activities.numElems();
        return activityRatingAverage;
    }
}
