package uoc.ded.practica;

import java.time.LocalDate;
import java.util.Date;

import uoc.ded.practica.exceptions.ActivityNotFoundException;
import uoc.ded.practica.exceptions.LimitExceededException;
import uoc.ded.practica.exceptions.NoActivitiesException;
import uoc.ded.practica.exceptions.NoRatingsException;
import uoc.ded.practica.exceptions.NoRecordsException;
import uoc.ded.practica.exceptions.OrganizationNotFoundException;
import uoc.ded.practica.exceptions.UserNotFoundException;
import uoc.ded.practica.exceptions.UserNotInActivityException;
import uoc.ded.practica.model.*;
import uoc.ded.practica.util.OrderedVector;
import uoc.ei.tads.*;

public class SafetyActivities4Covid19Impl implements SafetyActivities4Covid19 {

    private User[] users;
    private int numUsers;

    private Organization[] organizations;
    private int numOrganizations;

    private ColaConPrioridad<Record> records;
    private DiccionarioAVLImpl<String, Activity> activities;

    private DiccionarioAVLImpl<String, Order> orders;

    private int totalRecords;
    private int rejectedRecords;
    private int numRoles;
    private int numOrders;
    private int numGroups;

    private User mostActiveUser;
    private OrderedVector<Activity> bestActivity;

    private Role[] roles;

    private OrderedVector<Activity> bestActivitiesByRating;
    private OrderedVector<Organization> bestOrganizations;

    public SafetyActivities4Covid19Impl() {
        users = new User[U];
        numUsers = 0;
        numRoles = 0;
        numOrders = 0;
        numGroups = 0;
        organizations = new Organization[O];
        numOrganizations = 0;
        records = new ColaConPrioridad<Record>();
        activities = new DiccionarioAVLImpl<String, Activity>();
        totalRecords = 0;
        rejectedRecords = 0;
        mostActiveUser = null;
        bestActivity = new OrderedVector<Activity>(A, Activity.CMP_V);
        roles = new Role[MAX_NUMBER_OF_ROLES];
        bestActivitiesByRating = new OrderedVector<Activity>(A, Activity.CMP_V);
        bestOrganizations = new OrderedVector<Organization>(O, Organization.CMP_V);
    }

    public void addUser(String userId, String name, String surname, LocalDate birthday, boolean covidCertificate) {
        User u = getUser(userId);
        if (u != null) {
            u.setName(name);
            u.setSurname(surname);
            u.setBirthday(birthday);
            u.setCovidCertificate(covidCertificate);
        } else {
            u = new User(userId, name, surname, birthday, covidCertificate);
            addUser(u);
        }
    }

    public void addUser(User user) {
        users[numUsers++] = user;
    }

    public User getUser(String userId) {

        for (User u : users) {
            if (u == null) {
                return null;
            } else if (u.is(userId)){
                return u;
            }
        }
        return null;
    }


    public void addOrganization(int organizationId, String name, String description) {
        Organization organization = getOrganization(organizationId);
        if (organization != null) {
            organization.setName(name);
            organization.setDescription(description);
        } else {
            organization = new Organization(organizationId, name, description);
            organizations[organizationId]= organization;
            numOrganizations++;
        }
    }

    public Organization getOrganization(int organizationId) {
        return organizations[organizationId];
    }

    public void addRecord(String recordId, String actId, String description, Date date, Mode mode, int num, int organizationId) throws OrganizationNotFoundException{
        Organization organization = getOrganization(organizationId);
        if (organization == null) {
        	throw new OrganizationNotFoundException();
        }

        records.encolar(new Record(recordId, actId, description, date, mode, num, organization));
        totalRecords++;
    }

    public Record updateRecord(Status status, Date date, String description) throws NoRecordsException {
        Record record = records.desencolar();
        if (record  == null) {
        	throw new NoRecordsException();
        }

        record.update(status, date, description);
        if (record.isEnabled()) {
            Activity activity = record.newActivity();
            activities.insertar(activity.getActId(), activity);
        }
        else {
        	rejectedRecords++;
        }
        return record;
    }

    public Record currentRecord() {
        return (records.numElems() > 0 ? records.primero() : null);
    }

    public Iterador<Activity> getActivitiesByOrganization(int organizationId) throws NoActivitiesException {
        Organization organization = getOrganization(organizationId);

        if (!organization.hasActivities()) {
        	throw new NoActivitiesException();
        }
        return organization.activities();
    }

    public Iterador<Activity> getActivitiesByUser(String userId) throws NoActivitiesException {
        User user = getUser(userId);

        if (!user.hasActivities()) {
        	throw new NoActivitiesException();
        }
        return user.activities();
    }


    public Iterador<Activity> getAllActivities() throws NoActivitiesException {
        if (activities.numElems() == 0) {
        	throw new NoActivitiesException();
        }
        return activities.elementos();
    }


    public double getInfoRejectedRecords() {
        return (double)rejectedRecords / totalRecords;
    }

    public void createTicket(String userId, String actId) throws UserNotFoundException,
            ActivityNotFoundException, LimitExceededException {

        User user = getUser(userId);
        if (user == null) {
        	throw new UserNotFoundException();
        }

        Activity activity = getActivity(actId);
        if (activity  == null) {
        	throw new ActivityNotFoundException();
        }

        if (!activity.hasAvailabilityOfTickets()) {
        	throw new LimitExceededException();
        }

        activity.addTicket(user);
        user.addActivity(activity);
        updateMostActiveUser(user);
    }


    public Order assignSeat(String actId) throws ActivityNotFoundException {
        return null;
    }

    public void addRating(String actId, Rating rating, String message, String userId)
            throws ActivityNotFoundException, UserNotFoundException, UserNotInActivityException {
        Activity activity = getActivity(actId);
        if (activity == null) {
        	throw new ActivityNotFoundException();
        }

        User user = getUser(userId);
        if (user == null) {
        	throw new UserNotFoundException();
        }

        if (!user.isInActivity(actId)) {
        	throw new UserNotInActivityException();
        }

        activity.addRating(rating, message, user);
        updateBestActivity(activity);
    }

    private void updateBestActivity(Activity activity) {
        bestActivity.delete(activity);
        bestActivity.update(activity);
    }


    public Iterador<uoc.ded.practica.model.Rating> getRatings(String actId) throws ActivityNotFoundException, NoRatingsException {
        Activity activity = getActivity(actId);
        if (activity  == null) {
        	throw new ActivityNotFoundException();
        }

        if (!activity.hasRatings()) {
        	throw new NoRatingsException();
        }

        return activity.ratings();
    }


    private void updateMostActiveUser(User user) {
        if (mostActiveUser == null) {
            mostActiveUser = user;
        }
        else if (user.numActivities() > mostActiveUser.numActivities()) {
            mostActiveUser = user;
        }
    }


    public User mostActiveUser() throws UserNotFoundException {
        if (mostActiveUser == null) {
        	throw new UserNotFoundException();
        }

        return mostActiveUser;
    }

    public Activity bestActivity() throws ActivityNotFoundException {
        if (bestActivity.numElems() == 0) {
        	throw new ActivityNotFoundException();
        }

        return bestActivity.elementAt(0);
    }

    public int numUsers() {
        return numUsers;
    }

    public int numOrganizations() {
        return numOrganizations;
    }

    public int numPendingRecords() {
        return records.numElems();
    }

    public int numRecords() {
        return totalRecords;
    }

    public int numRejectedRecords() {
        return rejectedRecords;
    }

    public int numActivities() {
        return activities.numElems();
    }

    public int numActivitiesByOrganization(int organizationId) {
        Organization organization = getOrganization(organizationId);

        return organization.numActivities();
    }

    public Activity getActivity(String actId) {
        return activities.consultar(actId);
    }


    public int availabilityOfTickets(String actId) {
        Activity activity = getActivity(actId);

        return (activity != null ? activity.availabilityOfTickets() : 0);
    }

    public int numGroups() {
        return this.numGroups;
    }

    public int numOrders() {
        return this.numOrders;
    }

    public int numRoles() {
        return this.numRoles;
    }

}
