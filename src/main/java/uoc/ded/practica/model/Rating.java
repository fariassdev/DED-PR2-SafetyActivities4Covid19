package uoc.ded.practica.model;

import uoc.ded.practica.SafetyActivities4Covid19;

public class Rating {
    private SafetyActivities4Covid19.Rating rating;
    private String message;
    private User user;

    public Rating(SafetyActivities4Covid19.Rating rating, String message, User user) {
        this.rating = rating;
        this.message = message;
        this.user = user;
    }

    public SafetyActivities4Covid19.Rating getRating() {
        return this.rating;
    }

    public User getUser() {
        return this.user;
    }
}
