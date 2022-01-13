package uoc.ded.practica.model;

import uoc.ei.tads.Cola;
import uoc.ei.tads.ColaConPrioridad;
import uoc.ei.tads.Iterador;

public class Order {
    private String orderId;
    private User user;
    private Activity activity;
    private ColaConPrioridad<Ticket> tickets;
    private int seat;
    private int nextSeat;


    public Order(String orderId, User user, Activity activity, ColaConPrioridad<Ticket> tickets, int seat) {
        this.orderId = orderId;
        this.user = user;
        this.activity = activity;
        this.tickets = tickets;
        this.seat = seat;
        this.nextSeat = 1;
    }

    public int getValue() {
        return 0;
    }

    public String getId() {
        return this.orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void addTicket(User user) {
        tickets.encolar(new Ticket(user, this.activity));
    }

    public Ticket pop() {
        Ticket t = tickets.desencolar();
        t.setSeat(nextSeat++);
        return t;
    }

    public Iterador<Ticket> tickets() {
        return tickets.elementos();
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }
}
