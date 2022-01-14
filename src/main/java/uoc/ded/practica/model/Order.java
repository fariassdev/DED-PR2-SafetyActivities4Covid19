package uoc.ded.practica.model;

import uoc.ei.tads.Iterador;
import uoc.ei.tads.ListaEncadenada;

import java.util.Comparator;

public class Order {
    public static final Comparator<Order> CMP_V = (Order o1, Order o2)-> Double.compare(o2.getValue(), o1.getValue());

    private String orderId;
    private Group group;
    private User user;
    private Activity activity;
    private ListaEncadenada<Ticket> tickets;
    private Double value;

    public Order(String orderId, Group group, Activity activity, ListaEncadenada<Ticket> tickets) {
        this.orderId = orderId;
        this.group = group;
        this.activity = activity;
        this.tickets = tickets;
        this.value = 0.;
    }

    public Order(String orderId, User user, Activity activity, ListaEncadenada<Ticket> tickets) {
        this.orderId = orderId;
        this.user = user;
        this.activity = activity;
        this.tickets = tickets;
        this.value = 0.;
    }

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Iterador<Ticket> tickets() {
        return tickets.elementos();
    }
}
