package uoc.ded.practica;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import uoc.ded.practica.exceptions.*;
import uoc.ded.practica.model.*;
import uoc.ded.practica.util.OrderedVector;
import uoc.ei.tads.*;

public class SafetyActivities4Covid19Impl implements SafetyActivities4Covid19 {

    private TablaDispersion<String, User> users;
    private int numUsers;

    private TablaDispersion<String, Organization> organizations;
    private int numOrganizations;

    private ColaConPrioridad<Record> records;
    private DiccionarioAVLImpl<String, Activity> activities;
    private DiccionarioAVLImpl<String, Order> orders;
    private DiccionarioAVLImpl<String, Group> groups;

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
        users = new TablaDispersion<String, User>(U);
        numUsers = 0;
        numRoles = 0;
        numOrders = 0;
        numGroups = 0;
        organizations = new TablaDispersion<String, Organization>(O);
        numOrganizations = 0;
        records = new ColaConPrioridad<Record>(Record.CMP_V);
        activities = new DiccionarioAVLImpl<String, Activity>();
        orders = new DiccionarioAVLImpl<String, Order>();
        groups = new DiccionarioAVLImpl<String, Group>();
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
        users.insertar(user.getId(), user);
    }

    public User getUser(String userId) {
        return this.users.consultar(userId);
    }

    public void addOrganization(String organizationId, String name, String description) {
        Organization organization = getOrganization(organizationId);
        if (organization != null) {
            organization.setName(name);
            organization.setDescription(description);
        } else {
            organization = new Organization(organizationId, name, description);
            organizations.insertar(organizationId, organization);
            numOrganizations++;
        }
    }

    public Organization getOrganization(String organizationId) {
        return organizations.consultar(organizationId);
    }

    public void addRecord(String recordId, String actId, String description, Date date, LocalDate dateRecord, Mode mode, int num, String organizationId) throws OrganizationNotFoundException{
        Organization organization = getOrganization(organizationId);
        if (organization == null) {
        	throw new OrganizationNotFoundException();
        }

        records.encolar(new Record(recordId, actId, description, date, dateRecord, mode, num, organization));
        totalRecords++;
    }

    public Record updateRecord(Status status, Date date, String description) throws NoRecordsException {
        if (records == null || records.estaVacio()) {
            throw new NoRecordsException();
        }
        Record record = records.desencolar();
        if (record == null) {
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

    public Iterador<Activity> getActivitiesByOrganization(String organizationId) throws NoActivitiesException {
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

    public Order createTicket(String userId, String actId, LocalDate date) throws UserNotFoundException,
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

        // TODO: Extract to generateOrderId(LocalDate date, String userId) method
        final String template = "O-%s-%s";
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        final String orderId = String.format(template, date.format(dateFormatter), user.getId());

        ListaEncadenada<Ticket> tickets = new ListaEncadenada<Ticket>();
        Ticket ticket = new Ticket(user, activity);
        tickets.insertarAlPrincipio(ticket);
        Order order = new Order(orderId, user, activity, tickets);
        order.setValue((double) user.getBadge(date).getValue());
        this.orders.insertar(orderId, order);
        activity.addOrder(order);
        user.addActivity(activity);
        updateMostActiveUser(user);
        activity.addUser( user );

        return order;
    }


    public Order assignSeat(String actId) throws ActivityNotFoundException {
        Activity activity = this.activities.consultar(actId);
        if (activity == null) {
            throw new ActivityNotFoundException();
        }
        return activity.pop();
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

    public int numRecordsByOrganization(String organizationId) {
        return 0;
    }

    public int numActivitiesByOrganization(String organizationId) {
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

    public int numWorkersByRole(String roleId) {
        return 0;
    }

    public Role getRole(String roleId) {
        boolean found = false;

        int i = 0;
        while (!found && i < this.numRoles()) {
            found = this.roles[i].is(roleId);
            i++;
        }

        return (found ? this.roles[i - 1] : null);
    }

    public Group getGroup(String groupId) {
        return this.groups.consultar( groupId );
    }

    public int numWorkers() {
        return 0;
    }

    public int numWorkers(String organizationId) {
        return 0;
    }

    public void addRole(String roleId, String name) {
        Role role = this.getRole(roleId);

        if (role == null) {
            this.roles[this.numRoles++] = new Role(roleId, name);
        } else {
            role.setName(name);
        }
    }

    public void addWorker(String userId, String name, String surname, LocalDate birthday, boolean covidCertificate, String roleId, String organizationId) {

    }

    public Iterador<Worker> getWorkersByOrganization(String organizationId) throws OrganizationNotFoundException, NoWorkersException {
        return null;
    }

    public Iterador<User> getUsersInActivity(String activityId) throws ActivityNotFoundException, NoUserException {
        Activity activity = this.activities.consultar( activityId );
        if (activity == null) {
            throw new ActivityNotFoundException();
        }

        return activity.users();
    }

    public Badge getBadge(String userId, LocalDate day) throws UserNotFoundException {
        return this.getUser(userId).getBadge(day);
    }

    public void addGroup(String groupId, String description, LocalDate date, String... members) {
        Group group = this.groups.consultar(groupId);
        ListaEncadenada<User> memberList = new ListaEncadenada<User>();
        for (String userId : members) {
            User user = this.users.consultar(userId);
            memberList.insertarAlFinal(user);
        }
        if (group == null) {
            group = new Group(groupId, description, date, memberList);
            this.groups.insertar(groupId, group);
            numGroups++;
        } else {
            group.setDescription(description);
            group.setDate(date);
            group.setMembers(memberList);
        }
    }

    public Iterador<User> membersOf(String groupId) throws GroupNotFoundException, NoUserException {
        Group group = this.groups.consultar(groupId);
        if (group == null) {
            throw new GroupNotFoundException();
        }

        if (group.numMembers() == 0) {
            throw new NoUserException();
        }

        return group.members();
    }

    public double valueOf(String groupId) throws GroupNotFoundException {
        Group group = this.groups.consultar(groupId);
        if (group == null) {
            throw new GroupNotFoundException();
        }
        return group.valueOf();
    }

    public Order createTicketByGroup(String groupId, String actId, LocalDate date) throws GroupNotFoundException, ActivityNotFoundException, LimitExceededException {
        Group group = this.groups.consultar(groupId);
        if (group == null) {
            throw new GroupNotFoundException();
        }
        Activity activity = this.activities.consultar(actId);
        if (activity == null) {
            throw new ActivityNotFoundException();
        }

        if (!activity.hasAvailabilityOfTickets()) {
            throw new LimitExceededException();
        }

        ListaEncadenada<Ticket> tickets = new ListaEncadenada<Ticket>();

        for (Iterador<User> it = group.members(); it.haySiguiente();) {
            User user = it.siguiente();
            Ticket ticket = new Ticket(user, activity);
            tickets.insertarAlPrincipio(ticket);
            user.addActivity(activity);
            updateMostActiveUser(user);
            activity.addUser( user );
        }

        // TODO: Extract to generateOrderId(LocalDate date, String userId) method
        final String template = "O-%s-%s";
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        final String orderId = String.format(template, date.format(dateFormatter), group.getGroupId());

        Order order = new Order(orderId, group, activity, tickets);
        order.setValue(group.valueOf());
        this.orders.insertar(orderId, order);
        activity.addOrder(order);

        return order;
    }

    public Order getOrder(String orderId) throws OrderNotFoundException {
        Order order = this.orders.consultar( orderId );
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return order;
    }

    public Iterador<Worker> getWorkersByRole(String roleId) throws NoWorkersException {
        return null;
    }

    public Iterador<Record> getRecordsByOrganization(String organizationId) throws NoRecordsException {
        return null;
    }

    public Iterador<Organization> best5Organizations() throws NoOrganizationException {
        return null;
    }

    public Iterador<Activity> best10Activities() throws ActivityNotFoundException {
        return null;
    }

    public Worker getWorker(String workerId) {
        return null;
    }

}
