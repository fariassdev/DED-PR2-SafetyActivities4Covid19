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
    private int numWorkers;
    private User mostActiveUser;

    private TablaDispersion<String, Organization> organizations;
    private int numOrganizations;
    private OrderedVector<Organization> bestOrganizations;

    private ColaConPrioridad<Record> records;
    private int totalRecords;
    private int rejectedRecords;

    private DiccionarioAVLImpl<String, Activity> activities;
    private OrderedVector<Activity> bestActivity;

    private DiccionarioAVLImpl<String, Order> orders;
    private int numOrders;

    private DiccionarioAVLImpl<String, Group> groups;
    private int numGroups;

    private Role[] roles;
    private int numRoles;

    public SafetyActivities4Covid19Impl() {
        activities = new DiccionarioAVLImpl<String, Activity>();
        orders = new DiccionarioAVLImpl<String, Order>();
        groups = new DiccionarioAVLImpl<String, Group>();
        users = new TablaDispersion<String, User>( U );
        organizations = new TablaDispersion<String, Organization>( O );
        records = new ColaConPrioridad<Record>( Record.CMP_V );
        roles = new Role[ R ];
        bestActivity = new OrderedVector<Activity>( BEST_10_ACTIVITIES, Activity.CMP_V );
        bestOrganizations = new OrderedVector<Organization>( BEST_ORGANIZATIONS, Organization.CMP_V );
        mostActiveUser = null;
        numUsers = 0;
        numRoles = 0;
        numOrders = 0;
        numGroups = 0;
        numWorkers = 0;
        totalRecords = 0;
        rejectedRecords = 0;
        numOrganizations = 0;
    }

    private String generateOrderId( LocalDate date, String customerId ) {
        final String template = "O-%s-%s";
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern( "yyyyMMdd" );
        return String.format( template, date.format( dateFormatter ), customerId );
    }

    public void addUser( String userId, String name, String surname, LocalDate birthday, boolean covidCertificate ) {
        User user = getUser( userId );
        if ( user != null ) {
            user.setName( name );
            user.setSurname( surname );
            user.setBirthday( birthday );
            user.setCovidCertificate( covidCertificate );
        } else {
            user = new User( userId, name, surname, birthday, covidCertificate );
            addUser( user );
        }
    }

    public void addUser( User user ) {
        users.insertar( user.getId(), user );
        numUsers++;
    }

    public void addWorker( Worker worker ) {
        users.insertar( worker.getId(), worker );
    }

    public User getUser( String userId ) {
        return this.users.consultar( userId );
    }

    public void addOrganization( String organizationId, String name, String description ) {
        Organization organization = getOrganization( organizationId );
        if ( organization != null ) {
            organization.setName( name );
            organization.setDescription( description );
        } else {
            organization = new Organization( organizationId, name, description );
            this.addOrganization( organization );
        }
    }

    public Organization getOrganization( String organizationId ) {
        return organizations.consultar( organizationId );
    }

    public void addOrganization(Organization organization) {
        this.organizations.insertar( organization.getOrganizationId(), organization );
        numOrganizations++;
    }

    public void addRecord( String recordId, String actId, String description, Date date, LocalDate dateRecord, Mode mode, int num, String organizationId ) throws OrganizationNotFoundException {
        Organization organization = getOrganization( organizationId );
        if ( organization == null ) {
            throw new OrganizationNotFoundException();
        }

        final Record record = new Record( recordId, actId, description, date, dateRecord, mode, num, organization );
        records.encolar( record );
        organization.addRecord( record );
        updateBestOrganization( organization );
        totalRecords++;
    }

    public Record updateRecord( Status status, Date date, String description ) throws NoRecordsException {
        if ( records == null || records.estaVacio() ) {
            throw new NoRecordsException();
        }
        Record record = records.desencolar();
        if ( record == null ) {
            throw new NoRecordsException();
        }

        record.update( status, date, description );
        if ( record.isEnabled() ) {
            Activity activity = record.newActivity();
            this.addActivity( activity );
        } else {
            rejectedRecords++;
        }
        return record;
    }

    public Record currentRecord() {
        return (records.numElems() > 0 ? records.primero() : null);
    }

    public Iterador<Activity> getActivitiesByOrganization( String organizationId ) throws NoActivitiesException {
        Organization organization = getOrganization( organizationId );

        if ( !organization.hasActivities() ) {
            throw new NoActivitiesException();
        }
        return organization.activities();
    }

    public Iterador<Activity> getActivitiesByUser( String userId ) throws NoActivitiesException {
        User user = getUser( userId );

        if ( !user.hasActivities() ) {
            throw new NoActivitiesException();
        }
        return user.activities();
    }


    public Iterador<Activity> getAllActivities() throws NoActivitiesException {
        if ( activities.numElems() == 0 ) {
            throw new NoActivitiesException();
        }
        return activities.elementos();
    }


    public double getInfoRejectedRecords() {
        return (double) rejectedRecords / totalRecords;
    }

    public Order createTicket( String userId, String actId, LocalDate date ) throws UserNotFoundException,
            ActivityNotFoundException, LimitExceededException {

        User user = getUser( userId );
        if ( user == null ) {
            throw new UserNotFoundException();
        }

        Activity activity = getActivity( actId );
        if ( activity == null ) {
            throw new ActivityNotFoundException();
        }

        if ( !activity.hasAvailabilityOfTickets() ) {
            throw new LimitExceededException();
        }

        final String orderId = generateOrderId( date, user.getId() );

        ListaEncadenada<Ticket> tickets = new ListaEncadenada<Ticket>();
        Ticket ticket = new Ticket( user, activity );
        tickets.insertarAlPrincipio( ticket );
        Order order = new Order( orderId, user, activity, tickets );
        order.setValue( (double) user.getBadge( date ).getValue() );
        this.addOrder( order );
        this.numOrders++;
        activity.addOrder( order );
        user.addActivity( activity );
        updateMostActiveUser( user );
        activity.addUser( user );

        return order;
    }

    public Order assignSeat( String actId ) throws ActivityNotFoundException {
        Activity activity = this.getActivity( actId );
        if ( activity == null ) {
            throw new ActivityNotFoundException();
        }
        return activity.pop();
    }

    public void addRating( String actId, Rating rating, String message, String userId )
            throws ActivityNotFoundException, UserNotFoundException, UserNotInActivityException {
        Activity activity = getActivity( actId );
        if ( activity == null ) {
            throw new ActivityNotFoundException();
        }

        User user = getUser( userId );
        if ( user == null ) {
            throw new UserNotFoundException();
        }

        if ( !user.isInActivity( actId ) ) {
            throw new UserNotInActivityException();
        }

        activity.addRating( rating, message, user );
        updateBestActivity( activity );
    }

    private void updateBestActivity( Activity activity ) {
        bestActivity.delete( activity );
        bestActivity.update( activity );
    }

    private void updateBestOrganization( Organization organization ) {
        bestOrganizations.delete( organization );
        bestOrganizations.update( organization );
    }

    public Iterador<uoc.ded.practica.model.Rating> getRatings( String actId ) throws ActivityNotFoundException, NoRatingsException {
        Activity activity = getActivity( actId );
        if ( activity == null ) {
            throw new ActivityNotFoundException();
        }

        if ( !activity.hasRatings() ) {
            throw new NoRatingsException();
        }

        return activity.ratings();
    }


    private void updateMostActiveUser( User user ) {
        if ( mostActiveUser == null ) {
            mostActiveUser = user;
        } else if ( user.numActivities() > mostActiveUser.numActivities() ) {
            mostActiveUser = user;
        }
    }


    public User mostActiveUser() throws UserNotFoundException {
        if ( mostActiveUser == null ) {
            throw new UserNotFoundException();
        }

        return mostActiveUser;
    }

    public Activity bestActivity() throws ActivityNotFoundException {
        if ( bestActivity.numElems() == 0 ) {
            throw new ActivityNotFoundException();
        }

        return bestActivity.elementAt( 0 );
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

    public int numRecordsByOrganization( String organizationId ) {
        Organization organization = this.getOrganization( organizationId );
        return organization.numRecords();
    }

    public int numActivitiesByOrganization( String organizationId ) {
        Organization organization = getOrganization( organizationId );

        return organization.numActivities();
    }

    public Activity getActivity( String actId ) {
        return activities.consultar( actId );
    }

    public void addActivity( Activity activity ) {
        activities.insertar( activity.getActId(), activity );
    }

    public int availabilityOfTickets( String actId ) {
        Activity activity = getActivity( actId );

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

    public int numWorkersByRole( String roleId ) {
        for ( Role role : this.roles ) {
            if ( role.getRoleId().equals( roleId ) ) {
                return role.numWorkers();
            }
        }
        return 0;
    }

    public Role getRole( String roleId ) {
        boolean found = false;

        int i = 0;
        while ( !found && i < this.numRoles() ) {
            found = this.roles[ i ].is( roleId );
            i++;
        }

        return (found ? this.roles[ i - 1 ] : null);
    }

    public Group getGroup( String groupId ) {
        return this.groups.consultar( groupId );
    }

    public int numWorkers() {
        return this.numWorkers;
    }

    public int numWorkers( String organizationId ) {
        Organization organization = this.getOrganization( organizationId );
        return organization.numWorkers();
    }

    public void addRole( String roleId, String name ) {
        Role role = this.getRole( roleId );

        if ( role == null ) {
            this.roles[ this.numRoles++ ] = new Role( roleId, name );
        } else {
            role.setName( name );
        }
    }

    public void addWorker( String userId, String name, String surname, LocalDate birthday, boolean covidCertificate, String roleId, String organizationId ) {
        User user = this.getUser( userId );
        Worker worker = (user instanceof Worker ? (Worker) user : null);
        Role role = this.getRole( roleId );
        Organization organization = this.getOrganization( organizationId );
        if ( user != null && worker != null ) {
            if ( !worker.getOrganization().getOrganizationId().equals( organizationId ) ) {
                worker.getOrganization().deleteWorker( worker.getId() );
                organization.addWorker( worker );
            }

            if ( !worker.getRoleId().equals( roleId ) ) {
                worker.getRole().deleteWorker( worker.getId() );
                role.addWorker( worker );
            }
        }

        Worker newWorker = new Worker( userId, name, surname, birthday, covidCertificate, role, organization );

        if ( user == null ) {
            numUsers++;
            numWorkers++;
            organization.addWorker( newWorker );
            role.addWorker( newWorker );
        } else if ( worker == null ) {
            numWorkers++;
            role.addWorker( newWorker );
            organization.addWorker( newWorker );
        }

        this.addWorker( newWorker );
    }

    public Iterador<Worker> getWorkersByOrganization( String organizationId ) throws OrganizationNotFoundException, NoWorkersException {
        Organization organization = this.getOrganization( organizationId );
        if ( organization == null ) {
            throw new OrganizationNotFoundException();
        }

        if ( !organization.hasWorkers() ) {
            throw new NoWorkersException();
        }

        return organization.workers();
    }

    public Iterador<User> getUsersInActivity( String activityId ) throws ActivityNotFoundException, NoUserException {
        Activity activity = this.getActivity( activityId );
        if ( activity == null ) {
            throw new ActivityNotFoundException();
        }

        return activity.users();
    }

    public Badge getBadge( String userId, LocalDate day ) throws UserNotFoundException {
        return this.getUser( userId ).getBadge( day );
    }

    public void addGroup( Group group ) {
        this.groups.insertar( group.getGroupId(), group );
        numGroups++;
    }

    public void addGroup( String groupId, String description, LocalDate date, String... members ) {
        Group group = this.getGroup( groupId );
        ListaEncadenada<User> memberList = new ListaEncadenada<User>();
        for ( String userId : members ) {
            User user = this.getUser( userId );
            memberList.insertarAlFinal( user );
        }
        if ( group == null ) {
            group = new Group( groupId, description, date, memberList );
            this.addGroup( group );
        } else {
            group.setDescription( description );
            group.setDate( date );
            group.setMembers( memberList );
        }
    }

    public Iterador<User> membersOf( String groupId ) throws GroupNotFoundException, NoUserException {
        Group group = this.getGroup( groupId );
        if ( group == null ) {
            throw new GroupNotFoundException();
        }

        if ( group.numMembers() == 0 ) {
            throw new NoUserException();
        }

        return group.members();
    }

    public double valueOf( String groupId ) throws GroupNotFoundException {
        Group group = this.getGroup( groupId );
        if ( group == null ) {
            throw new GroupNotFoundException();
        }
        return group.valueOf();
    }

    public Order createTicketByGroup( String groupId, String actId, LocalDate date ) throws GroupNotFoundException, ActivityNotFoundException, LimitExceededException {
        Group group = this.getGroup( groupId );
        if ( group == null ) {
            throw new GroupNotFoundException();
        }
        Activity activity = this.getActivity( actId );
        if ( activity == null ) {
            throw new ActivityNotFoundException();
        }

        if ( !activity.hasAvailabilityOfTickets() ) {
            throw new LimitExceededException();
        }

        ListaEncadenada<Ticket> tickets = new ListaEncadenada<Ticket>();

        for ( Iterador<User> it = group.members(); it.haySiguiente(); ) {
            User user = it.siguiente();
            Ticket ticket = new Ticket( user, activity );
            tickets.insertarAlPrincipio( ticket );
            user.addActivity( activity );
            updateMostActiveUser( user );
            activity.addUser( user );
        }

        final String orderId = generateOrderId( date, group.getGroupId() );

        Order order = new Order( orderId, group, activity, tickets );
        order.setValue( group.valueOf() );
        this.addOrder( order );
        activity.addOrder( order );

        return order;
    }

    public Order getOrder( String orderId ) throws OrderNotFoundException {
        Order order = this.orders.consultar( orderId );
        if ( order == null ) {
            throw new OrderNotFoundException();
        }
        return order;
    }

    public void addOrder( Order order ) {
        this.orders.insertar( order.getId(), order );
    }

    public Iterador<Worker> getWorkersByRole( String roleId ) throws NoWorkersException {
        for ( Role role : this.roles ) {
            if ( role.getRoleId().equals( roleId ) ) {
                if ( !role.hasWorkers() ) {
                    throw new NoWorkersException();
                }
                return role.workers();
            }
        }
        return null;
    }

    public Iterador<Record> getRecordsByOrganization( String organizationId ) throws NoRecordsException {
        Organization organization = this.getOrganization( organizationId );
        if ( organization.numRecords() == 0 ) {
            throw new NoRecordsException();
        }
        return organization.records();

    }

    public Iterador<Organization> best5Organizations() throws NoOrganizationException {
        if ( this.organizations.estaVacio() ) {
            throw new NoOrganizationException();
        }

        return bestOrganizations.elementos();
    }

    public Iterador<Activity> best10Activities() throws ActivityNotFoundException {
        if ( this.activities.estaVacio() ) {
            throw new ActivityNotFoundException();
        }
        return bestActivity.elementos();
    }

    public Worker getWorker( String workerId ) {
        return (Worker) this.getUser( workerId );
    }

}
