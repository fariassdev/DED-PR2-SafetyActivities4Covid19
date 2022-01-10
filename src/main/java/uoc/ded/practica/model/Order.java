package uoc.ded.practica.model;

public class Order {
    private User user;
    private Activity activity;
    private int seat;


    public Order(User user, Activity activity) {
        this.user = user;
        this.activity = activity;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "**" + activity.getActId() + " " + seat + " " + user.getId();
    }

    public User getUser() {
        return user;
    }

    public Activity getActivity() {
        return activity;
    }
}
