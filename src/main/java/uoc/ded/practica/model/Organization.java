package uoc.ded.practica.model;

import uoc.ei.tads.Iterador;
import uoc.ei.tads.ListaEncadenada;
import uoc.ei.tads.Posicion;
import uoc.ei.tads.Recorrido;

import java.util.Comparator;

public class Organization {
    public static final Comparator<Organization> CMP_V = (Organization o1, Organization o2)->Double.compare(o1.getAverageActivityRating(), o2.getAverageActivityRating());

    private String organizationId;
    private String description;
    private String name;
    private ListaEncadenada<Activity> activities;
    private ListaEncadenada<Worker> workers;
    private ListaEncadenada<Record> records;

    public Organization(String organizationId, String name, String description) {
        this.organizationId = organizationId;
        this.name = name;
        this.description = description;
        activities = new ListaEncadenada<Activity>();
        workers = new ListaEncadenada<Worker>();
        records = new ListaEncadenada<Record>();
    }

    public String getName() {
        return name;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getDescription() {
        return description;
    }

    public void setOrganizationId(String organizationId) {
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

    public Iterador<Worker> workers() {
        return workers.elementos();
    }

    public Recorrido<Worker> workerPositions() {
        return workers.posiciones();
    }

    public void addWorker( Worker user) {
        workers.insertarAlFinal(user);
    }

    public void deleteWorker( String workerId ) {
        for ( Recorrido<Worker> it = this.workerPositions(); it.haySiguiente(); ) {
            final Posicion<Worker> workerPosition = it.siguiente();
            Worker worker = workerPosition.getElem();
            if ( worker.getId().equals( workerId ) ) {
                workers.borrar( workerPosition );
            }

        }
    }

    public boolean hasWorkers() {
        return workers.numElems() > 0;
    }

    public int numWorkers() {
        return workers.numElems();
    }

    public Iterador<Record> records() {
        return records.elementos();
    }

    public void addRecord( Record record) {
        records.insertarAlFinal(record);
    }

    public int numRecords() {
        return records.numElems();
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
