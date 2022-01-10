package uoc.ded.practica;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ded.practica.exceptions.DEDException;
import uoc.ded.practica.exceptions.LimitExceededException;
import uoc.ded.practica.exceptions.NoActivitiesException;
import uoc.ded.practica.exceptions.OrganizationNotFoundException;
import uoc.ded.practica.model.*;

import uoc.ded.practica.util.DateUtils;
import uoc.ei.tads.Iterador;


public class SafetyActivities4Covid19PR1Test extends SafetyActivities4Covid19PR {

    /**
     * *feature*: (sobre la que hacemos @Test): addUser del TAD SafetyActivities4Covid19
     * *given*: Hay 19 usuarios en el sistema
     * *scenario*:
     * - Se añade un nuevo usuario en el sistema
     * - Se añade un segundo usuario en el sistema
     * - Se modifican los datos del segundo usuario insertar
     */
    @Test
    public void testAddUser() {

        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        //

        safetyActivities4Covid19.addUser("idUser1000", "Robert", "Lopez", createLocalDate("02-01-1942"), false);
        Assert.assertEquals("Robert", safetyActivities4Covid19.getUser("idUser1000").getName());
        Assert.assertEquals(20, safetyActivities4Covid19.numUsers());

        safetyActivities4Covid19.addUser("idUser9999", "XXXXX", "YYYYY", createLocalDate("12-11-1962"), true);
        Assert.assertEquals("XXXXX", safetyActivities4Covid19.getUser("idUser9999").getName());
        Assert.assertEquals(21, safetyActivities4Covid19.numUsers());

        safetyActivities4Covid19.addUser("idUser9999", "Lluis", "Casals", createLocalDate("22-07-1938"), true);
        Assert.assertEquals("Lluis", safetyActivities4Covid19.getUser("idUser9999").getName());
        Assert.assertEquals("Casals", safetyActivities4Covid19.getUser("idUser9999").getSurname());
        Assert.assertEquals(21, safetyActivities4Covid19.numUsers());
    }


    /**
     * *feature*: (sobre la que hacemos @Test): addOrganization del TAD SafetyActivities4Covid19
     * *given*: Hay 19 usuarios en el sistema y 5 organizaciones
     * *scenario*:
     * - Se añade una nueva organización en el sistema
     * - Se añade una segunda organización en el sistema
     * - Se modifican los datos de la segunda organización
     */
    @Test
    public void testAddOrganization() {

        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        //

        safetyActivities4Covid19.addOrganization("15", "ORG_VDA", "description VDA" );
        Assert.assertEquals("ORG_VDA", safetyActivities4Covid19.getOrganization("15").getName());
        Assert.assertEquals("description VDA", safetyActivities4Covid19.getOrganization("15").getDescription());
        Assert.assertEquals(6, safetyActivities4Covid19.numOrganizations());

        safetyActivities4Covid19.addOrganization("17", "ORG_XXX", "description XXX" );
        Assert.assertEquals("ORG_XXX", safetyActivities4Covid19.getOrganization("17").getName());
        Assert.assertEquals("description XXX", safetyActivities4Covid19.getOrganization("17").getDescription());
        Assert.assertEquals(7, safetyActivities4Covid19.numOrganizations());

        safetyActivities4Covid19.addOrganization("17", "ORG_AWS", "description AW" );
        Assert.assertEquals("ORG_AWS", safetyActivities4Covid19.getOrganization("17").getName());
        Assert.assertEquals("description AW", safetyActivities4Covid19.getOrganization("17").getDescription());
        Assert.assertEquals(7, safetyActivities4Covid19.numOrganizations());
    }


    /**
     * *feature*: (sobre la que hacemos @Test): addRecord del TAD SafetyActivities4Covid19
     * *given*: Hay:
     *      - 19 usuarios en el sistema
     *      - 5 organizaciones
     *      - 6 Expedientes en el sistema
     *      - 1 Expediente pendiente de validar
     *      - 1 Expediente rechazado
     *      - 4 Actividades
     *
     * *scenario*:
     * - Se añade un nuevo expediente en el sistema
     * - Se añade un segundo expediente en el sistema
     */
    @Test
    public void testAddRecord() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //
        Record record=null;

        safetyActivities4Covid19.addRecord("XXX-001", "ACT-010", "description ACT-010" ,
                createDate("22-11-2022 23:00:00"), createLocalDate("24-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 100, "C-FURA");

        Assert.assertEquals(2, safetyActivities4Covid19.numPendingRecords() );

        record = safetyActivities4Covid19.currentRecord();
        Assert.assertEquals("XXX-001", record.getRecordId() );
        Assert.assertEquals(SafetyActivities4Covid19.Status.PENDING, record.getStatus() );


        safetyActivities4Covid19.addRecord("XXX-002", "ACT-011", "description ACT-011",
                createDate("25-11-2022 23:00:00"), createLocalDate("21-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 100, "C-FURA");

        Assert.assertEquals(3, safetyActivities4Covid19.numPendingRecords() );

        record = safetyActivities4Covid19.currentRecord();
        Assert.assertEquals("XXX-002", record.getRecordId() );
        Assert.assertEquals(SafetyActivities4Covid19.Status.PENDING, record.getStatus() );

    }


    /**
     * *feature*: (sobre la que hacemos @Test): addRecord del TAD SafetyActivities4Covid19
     * *given*: Hay:
     *      - 19 usuarios en el sistema
     *      - 5 organizaciones
     *      - 6 Expedientes en el sistema
     *      - 1 Expediente pendiente de validar
     *      - 1 Expediente rechazado
     *      - 4 Actividades
     * *scenario*:
     * - Se añade un nuevo expediente en el sistema sobre una organización inexistente
     */
    @Test(expected = OrganizationNotFoundException.class)
    public void testAddRecordAndOrganizationNotFound() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //

        safetyActivities4Covid19.addRecord("XXX-002", "ACT-011", "description ACT-011",
                createDate("25-11-2022 23:00:00"), createLocalDate("24-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 100, "2");

    }


    /**
     * *feature*: (sobre la que hacemos @Test): addRecord & update del TAD SafetyActivities4Covid19
     * *given*: Hay:
     *      - 19 usuarios en el sistema
     *      - 5 organizaciones
     *      - 6 Expedientes en el sistema
     *      - 1 Expediente pendiente de validar
     *      - 1 Expediente rechazado
     *      - 4 Actividades
     *
     * *scenario*:
     *   - Se añaden "records" del test testAddRecord
     */
    @Test
    public void testAddRecordAndUpdate() throws DEDException {

        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //

        this.testAddRecord();


        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(8, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(3, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //
        Record record=null;

        Assert.assertEquals(0.12, safetyActivities4Covid19.getInfoRejectedRecords(),0.03);

        record = safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.DISABLED,
                createDate("25-11-2021 23:00:00"), "KO X1");
        Assert.assertNull(record.getActivity());

        Assert.assertEquals(2, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(8, safetyActivities4Covid19.numRecords() );


        Assert.assertEquals(0.25, safetyActivities4Covid19.getInfoRejectedRecords(),0.03);

    }


    /**
     * *feature*: (sobre la que hacemos @Test): getActivitiesByOrganization del TAD SafetyActivities4Covid19
     * *given*: Hay:
     *      - 19 usuarios en el sistema
     *      - 5 organizaciones
     *      - 6 Expedientes en el sistema
     *      - 1 Expediente pendiente de validar
     *      - 1 Expediente rechazado
     *      - 4 Actividades
     *
     * *scenario*:
     *   - Se consultan las actividades de una organización
     */
    @Test
    public void testGetActivitiesByOrganization() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //


        Assert.assertEquals(3, safetyActivities4Covid19.numActivitiesByOrganization("C-DAGOLL"));

        Iterador<Activity> it = safetyActivities4Covid19.getActivitiesByOrganization("C-DAGOLL");
        Activity activity1 = it.siguiente();
        Assert.assertEquals("ACT-1103", activity1.getActId());

        Activity activity2 = it.siguiente();
        Assert.assertEquals("ACT-1101", activity2.getActId());
    }

    /**
     * *feature*: (sobre la que hacemos @Test): getActivitiesByOrganization del TAD SafetyActivities4Covid19
     * *given*: Hay:
     *      - 19 usuarios en el sistema
     *      - 5 organizaciones
     *      - 6 Expedientes en el sistema
     *      - 1 Expediente pendiente de validar
     *      - 1 Expediente rechazado
     *      - 4 Actividades
     *
     * *scenario*:
     *   - Se consultan las actividades de una organización que NO tiene actividades (3)
     */
    @Test(expected = NoActivitiesException.class)
    public void testGetActivitiesByOrganizationAndNOActiviesException() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //
        Iterador<Activity> it = safetyActivities4Covid19.getActivitiesByOrganization("C-FURA");
    }


    /**
     * *feature*: (sobre la que hacemos @Test): getAllActivities del TAD SafetyActivities4Covid19
     * *given*: Hay:
     *      - 10 usuarios en el sistema
     *      - 5 organizaciones
     *      - 6 Expedientes en el sistema
     *      - 1 Expediente pendiente de validar
     *      - 1 Expediente rechazado
     *      - 4 Actividades
     *
     * *scenario*:
     *   - Se consultan todas las actividades
     */
    @Test
    public void testGetAllActivities() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //
        Iterador<Activity> it = safetyActivities4Covid19.getAllActivities();

        Activity activity1 = it.siguiente();
        Assert.assertEquals("ACT-1101", activity1.getActId());

        Activity activity2 = it.siguiente();
        Assert.assertEquals("ACT-1102", activity2.getActId());

        Activity activity3 = it.siguiente();
        Assert.assertEquals("ACT-1103", activity3.getActId());

    }

    /**
     * *feature*: (sobre la que hacemos @Test): getAllActivitiesByUser del TAD SafetyActivities4Covid19
     * *given*: Hay:
     *      - 19 usuarios en el sistema
     *      - 5 organizaciones
     *      - 6 Expedientes en el sistema
     *      - 1 Expediente pendiente de validar
     *      - 1 Expediente rechazado
     *      - 4 Actividades
     *
     * *scenario*:
     *   - Se consultan las actividades de un usuario y no existe ninguna
     */
    @Test(expected = NoActivitiesException.class)
    public void testGetAllActivitiesByUserAndNoActivitiesException() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //
        Iterador<Activity> it = safetyActivities4Covid19.getActivitiesByUser("idUser9");

    }

    /**
     * *feature*: (sobre la que hacemos @Test): addRating & getRatings del TAD SafetyActivities4Covid19
     * *given*: Hay:
     *      - 19 usuarios en el sistema
     *      - 5 organizaciones
     *      - 6 Expedientes en el sistema
     *      - 1 Expediente pendiente de validar
     *      - 1 Expediente rechazado
     *      - 4 Actividades
     *
     * *scenario*:
     *   - Se añaden valoraciones sobre un par de actividades
     *   culturales que van alternando ser la mejor actividad cultural
     */
    @Test
    public void testAddRating() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //

        Activity activity1105 = safetyActivities4Covid19.getActivity("ACT-1105");

        Assert.assertEquals(0, activity1105.rating(),0);

        safetyActivities4Covid19.addRating("ACT-1105",
                SafetyActivities4Covid19.Rating.FIVE, "Very good", "idUser1");

        Assert.assertEquals(5, activity1105.rating(),0);

        safetyActivities4Covid19.addRating("ACT-1105",
                SafetyActivities4Covid19.Rating.FOUR, "Good", "idUser2");

        Assert.assertEquals(4.5, activity1105.rating(),0);

        safetyActivities4Covid19.addRating("ACT-1105",
                SafetyActivities4Covid19.Rating.TWO, "CHIPI - CHAPI", "idUser3");
        Assert.assertEquals(3.6, activity1105.rating(),0.09);

        Activity bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1105", bestActivity.getActId());
        //
        //

        Activity activity1102 = safetyActivities4Covid19.getActivity("ACT-1102");

        safetyActivities4Covid19.addRating("ACT-1102",
                SafetyActivities4Covid19.Rating.FOUR, "Good!!!", "idUser4");
        Assert.assertEquals(4, activity1102.rating(),0);
        Assert.assertEquals(3.6, activity1105.rating(),0.09);

        bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1102", bestActivity.getActId());



        safetyActivities4Covid19.addRating("ACT-1102",
                SafetyActivities4Covid19.Rating.ONE, "Bad!!!", "idUser5");
        Assert.assertEquals(2.5, activity1102.rating(),0.09);
        Assert.assertEquals(3.6, activity1105.rating(),0.09);

        bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1105", bestActivity.getActId());

        safetyActivities4Covid19.addRating("ACT-1102",
                SafetyActivities4Covid19.Rating.FOUR, "Good!!!", "idUser6");
        Assert.assertEquals(3, activity1102.rating(),0.09);
        Assert.assertEquals(3.6, activity1105.rating(),0.09);

        bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1105", bestActivity.getActId());

        safetyActivities4Covid19.addRating("ACT-1102",
                SafetyActivities4Covid19.Rating.FIVE, "Very Good!!!", "idUser7");
        Assert.assertEquals(3.5, activity1102.rating(),0);
        Assert.assertEquals(3.6, activity1105.rating(),0.09);

        bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1105", bestActivity.getActId());

        safetyActivities4Covid19.addRating("ACT-1102",
                SafetyActivities4Covid19.Rating.FIVE, "Very Good!!!", "idUser7");
        Assert.assertEquals(3.8, activity1102.rating(),0);
        Assert.assertEquals(3.6, activity1105.rating(),0.09);

        bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1102", bestActivity.getActId());


        Iterador<Rating> it = safetyActivities4Covid19.getRatings("ACT-1102");
        Rating rating = it.siguiente();
        Assert.assertEquals(SafetyActivities4Covid19.Rating.FOUR, rating.getRating());
        Assert.assertEquals("idUser4", rating.getUser().getId());



    }




    /**
     * *feature*: (sobre la que hacemos @Test): createTicket & assignSeat del TAD SafetyActivities4Covid19
     * *given*: Hay:
     *      - 19 usuarios en el sistema
     *      - 5 organizaciones
     *      - 6 Expedientes en el sistema
     *      - 1 Expediente pendiente de validar
     *      - 1 Expediente rechazado
     *      - 4 Actividades
     *      - 7 entradas compradas sobre una actividad
     *      - 4 asientos asignados
     *
     * *scenario*:
     *   - Se compra 1 entrada y se asigna su asiento
     */
    @Test
    public void testCreateTicketAndAssign() throws DEDException {

        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        Assert.assertEquals(7, safetyActivities4Covid19.numOrders());
        Assert.assertEquals(3, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));
        //

        Order o1 = safetyActivities4Covid19.createTicket("idUser8", "ACT-1102", DateUtils.createLocalDate("23-04-2021"));
        Assert.assertEquals(2, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        Order o2 = safetyActivities4Covid19.createTicket("idUser9", "ACT-1102", DateUtils.createLocalDate("23-04-2021"));
        Assert.assertEquals(1, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        Order o3 = safetyActivities4Covid19.createTicket("idUser10", "ACT-1102", DateUtils.createLocalDate("23-04-2021"));
        Assert.assertEquals(0, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        o2 = safetyActivities4Covid19.assignSeat("ACT-1102");
        Assert.assertEquals(SafetyActivities4Covid19.Badge.JUNIOR_PLUS.getValue(), o2.getValue(),0);

        Iterador<Ticket> it5 = o2.tickets();

        Ticket ticket5 = it5.siguiente();
        Assert.assertEquals(5, ticket5.getSeat());
        Assert.assertEquals("idUser9", ticket5.getUser().getId());

        o3 = safetyActivities4Covid19.assignSeat("ACT-1102");;
        Assert.assertEquals(SafetyActivities4Covid19.Badge.DARK.getValue(), o3.getValue(),0);
        Iterador<Ticket> it6 = o3.tickets();

        Ticket ticket6 = it6.siguiente();
        Assert.assertEquals(6, ticket6.getSeat());
        Assert.assertEquals("idUser10", ticket6.getUser().getId());

        o1 = safetyActivities4Covid19.assignSeat("ACT-1102");
        Assert.assertEquals(SafetyActivities4Covid19.Badge.DARK.getValue(), o1.getValue(),0);
        Iterador<Ticket> it7 = o1.tickets();

        Ticket ticket7 = it7.siguiente();
        Assert.assertEquals(7, ticket7.getSeat());
        Assert.assertEquals("idUser8", ticket7.getUser().getId());
    }


    /**
     * *feature*: (sobre la que hacemos @Test): createTicket  del TAD SafetyActivities4Covid19
     * *given*: Hay:
     *      - 19 usuarios en el sistema
     *      - 5 organizaciones
     *      - 6 Expedientes en el sistema
     *      - 1 Expediente pendiente de validar
     *      - 1 Expediente rechazado
     *      - 4 Actividades
     *      - 4 entradas compradas sobre una actividad
     *      - 4 asientos asignados
     *
     * *scenario*:
     *   - Se compran más de 50 entradas y se excede el número máximo de entradas
     */
    @Test(expected = LimitExceededException.class)
    public void testCreateTicketAndLimitExceededException() throws DEDException {

        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        Assert.assertEquals(3, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));
        //

        safetyActivities4Covid19.createTicket("idUser7", "ACT-1102", DateUtils.createLocalDate("23-04-2021") );
        Assert.assertEquals(2, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        safetyActivities4Covid19.createTicket("idUser8", "ACT-1102", DateUtils.createLocalDate("23-04-2021"));
        Assert.assertEquals(1, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        safetyActivities4Covid19.createTicket("idUser9", "ACT-1102", DateUtils.createLocalDate("23-04-2021"));
        Assert.assertEquals(0, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        safetyActivities4Covid19.createTicket("idUser10", "ACT-1102", DateUtils.createLocalDate("23-04-2021"));

    }


}
