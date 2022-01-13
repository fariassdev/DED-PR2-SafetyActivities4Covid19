package uoc.ded.practica.model;

import java.util.Comparator;
import java.util.Date;

import uoc.ded.practica.SafetyActivities4Covid19;
import uoc.ei.tads.*;

public class Activity implements Comparable<Activity> {
    public static final Comparator<String> CMP_K = (String o1, String o2)->o1.compareTo(o2);
    public static final Comparator<Activity> CMP_V = (Activity a1, Activity a2)->Double.compare(a1.rating(), a2.rating());

    private String actId;
    private String description;
    private Date date;
    private SafetyActivities4Covid19.Mode mode;
    private int total;
    private int availabilityOfTickets;
    private Record record;
    private ColaConPrioridad<Order> orders;
    private Lista<Rating> ratings;
    private int totalRatings;

    public Activity(String actId, String description, Date dateAct, SafetyActivities4Covid19.Mode mode, int num, Record record) {

        this.actId = actId;
        this.description = description;
        this.date = dateAct;
        this.mode = mode;
        this.total = num;
        this.availabilityOfTickets = num;
        this.record = record;
        orders = new ColaConPrioridad<Order>();
        ratings = new ListaEncadenada<Rating>();
    }

    public String getActId() {
        return actId;
    }


    public boolean hasAvailabilityOfTickets() {
        return (availabilityOfTickets > 0  );
    }

    public void addOrder(User user, ColaConPrioridad<Ticket> tickets, int seat) {
        orders.encolar(new Order("test", user, this, tickets, seat));
        availabilityOfTickets--;
    }

    public boolean is(String actId) {
        return this.actId.equals(actId);
    }

    public void addRating(SafetyActivities4Covid19.Rating rating, String message, User user) {
        Rating newRating = new Rating(rating, message, user);
        ratings.insertarAlFinal(newRating);
        totalRatings += rating.getValue();
    }

    public double rating() {
        return (ratings.numElems() != 0 ? (double)totalRatings / ratings.numElems() : 0);
    }

    public Iterador<Rating> ratings() {
        return ratings.elementos();
    }

    public boolean hasRatings() {
        return ratings.numElems() > 0;
    }

    public int numPendingTickets() {
        return orders.numElems();
    }

    public int availabilityOfTickets() {
        return availabilityOfTickets;
    }

    @Override
    public int compareTo(Activity o) {
        return actId.compareTo(o.actId);
    }
}
