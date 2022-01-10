package uoc.ded.practica.model;

import java.util.Comparator;
import java.util.Date;

import uoc.ded.practica.SafetyActivities4Covid19;
import uoc.ei.tads.Cola;
import uoc.ei.tads.ColaVectorImpl;
import uoc.ei.tads.Iterador;
import uoc.ei.tads.Lista;
import uoc.ei.tads.ListaEncadenada;

public class Activity implements Comparable<Activity> {
    public static final Comparator<String> CMP_K = (String o1, String o2)->o1.compareTo(o2);
    public static final Comparator<Activity> CMP_V = (Activity a1, Activity a2)->Double.compare(a1.rating(), a2.rating());

    private String actId;
    private String description;
    private Date date;
    private SafetyActivities4Covid19.Mode mode;
    private int total;
    private int nextSeat;
    private int availabilityOfTickets;
    private Record record;
    private Cola<Ticket> tickets;
    private Lista<Rating> ratings;
    private int totalRatings;

    public Activity(String actId, String description, Date dateAct, SafetyActivities4Covid19.Mode mode, int num, Record record) {

        this.actId = actId;
        this.description = description;
        this.date = dateAct;
        this.mode = mode;
        this.total = num;
        this.nextSeat = 1;
        this.availabilityOfTickets = num;
        this.record = record;
        tickets = new ColaVectorImpl<Ticket>();
        ratings = new ListaEncadenada<Rating>();
    }

    public String getActId() {
        return actId;
    }


    public boolean hasAvailabilityOfTickets() {
        return (availabilityOfTickets > 0  );
    }

    public void addTicket(User user) {
        tickets.encolar(new Ticket (user, this));
        availabilityOfTickets--;
    }

    public Ticket pop() {
        Ticket t = tickets.desencolar();
        t.setSeat(nextSeat++);
        return t;
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
        return tickets.numElems();
    }

    public int availabilityOfTickets() {
        return availabilityOfTickets;
    }

    @Override
    public int compareTo(Activity o) {
        return actId.compareTo(o.actId);
    }
}
