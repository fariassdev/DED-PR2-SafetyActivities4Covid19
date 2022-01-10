package uoc.ded.practica;


import org.junit.Assert;
import uoc.ded.practica.model.Activity;
import uoc.ded.practica.model.Order;
import uoc.ded.practica.model.Record;
import uoc.ded.practica.model.Ticket;
import uoc.ded.practica.util.DateUtils;
import uoc.ei.tads.Iterador;


public class FactorySafetyActivities4Covid19 {


    public static SafetyActivities4Covid19 getSafetyActivities4Covid19() throws Exception {
        SafetyActivities4Covid19 safetyActivities4Covid19;
        safetyActivities4Covid19 = new SafetyActivities4Covid19Impl();

        ////
        //// USERS
        ////
        safetyActivities4Covid19.addUser("idUser1", "Maria", "Simo", DateUtils.createLocalDate("07-01-1934"), true);
        safetyActivities4Covid19.addUser("idUser2", "Àlex", "Lluna", DateUtils.createLocalDate("08-10-1985"), true);
        safetyActivities4Covid19.addUser("idUser3", "Pepet", "Ferra", DateUtils.createLocalDate("30-03-1931"), false);
        safetyActivities4Covid19.addUser("idUser4", "Joana", "Quilez", DateUtils.createLocalDate("07-01-1924"), false);
        safetyActivities4Covid19.addUser("idUser5", "Armand", "Morata", DateUtils.createLocalDate("07-01-1942"),true);
        safetyActivities4Covid19.addUser("idUser6", "Jesus", "Sallent", DateUtils.createLocalDate("07-01-1932"), true);
        safetyActivities4Covid19.addUser("idUser7", "Anna", "Casals", DateUtils.createLocalDate("09-07-1988"), false);
        safetyActivities4Covid19.addUser("idUser8", "Mariajo", "Padró", DateUtils.createLocalDate("02-06-1992"), false);
        safetyActivities4Covid19.addUser("idUser9", "Agustí", "Padró", DateUtils.createLocalDate("15-01-2005"), true);
        safetyActivities4Covid19.addUser("idUser10", "Pepet", "Marieta", DateUtils.createLocalDate("23-04-2008"), false);
        safetyActivities4Covid19.addUser("idUser11", "Joanet", "Simó", DateUtils.createLocalDate("13-04-1942"), true);
        safetyActivities4Covid19.addUser("idUser12", "Carme", "Fonollosa", DateUtils.createLocalDate("18-02-1939"), true);

        ////
        //// ORGANIZATIONS
        ////
        safetyActivities4Covid19.addOrganization("C-FURA", "La fura dels Baus", "description La furra" );
        safetyActivities4Covid19.addOrganization("C-TRICICLE", "El tricicle", "description Tricicle" );
        safetyActivities4Covid19.addOrganization("C-DAGOLL", "Dagoll Dagom", "description Dagoll" );
        safetyActivities4Covid19.addOrganization("1", "ORG_XXXX", "description XXXXX" );
        safetyActivities4Covid19.addOrganization("16", "ORG_CDD", "description CDD" );

        ////
        //// RECORDS && ACTIVITIES
        ////
        safetyActivities4Covid19.addRecord("R-000", "ACT-1101", "description ACT-1101" ,
                DateUtils.createDate("22-12-2022 23:00:00"), DateUtils.createLocalDate("30-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 50, "C-DAGOLL");

        safetyActivities4Covid19.addRecord("R-001", "ACT-1101", "description ACT-1101" ,
                DateUtils.createDate("22-11-2022 23:00:00"), DateUtils.createLocalDate("24-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 50, "C-DAGOLL");

        safetyActivities4Covid19.addRecord("R-002", "ACT-1102", "description ACT-1102" ,
                DateUtils.createDate("22-11-2022 23:00:00"), DateUtils.createLocalDate("26-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 7, "C-DAGOLL");

        safetyActivities4Covid19.addRecord("R-003", "ACT-1103", "description ACT-1103" ,
                DateUtils.createDate("22-11-2022 23:00:00"),  DateUtils.createLocalDate("21-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 50, "C-DAGOLL");

        safetyActivities4Covid19.addRecord("R-004", "ACT-1104", "description ACT-1104" ,
                DateUtils.createDate("22-11-2022 23:00:00"),  DateUtils.createLocalDate("21-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 50, "C-DAGOLL");

        safetyActivities4Covid19.addRecord("R-005", "ACT-1105", "description ACT-1105" ,
                DateUtils.createDate("23-11-2022 23:00:00"),  DateUtils.createLocalDate("21-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 20, "C-TRICICLE");

        Activity activity1103 = safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.ENABLED,
                DateUtils.createDate("24-11-2021 11:00:00"), "OK: XXX 0").getActivity();
        Assert.assertEquals("ACT-1103", activity1103.getActId());

        Activity activity1105 = safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.ENABLED,
                DateUtils.createDate("24-10-2021 12:00:00"), "OK: XXX 1").getActivity();
        Assert.assertEquals("ACT-1105", activity1105.getActId());

        Record record1104 = safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.DISABLED,
                DateUtils.createDate("24-10-2021 14:00:00"), "KO: XXX");
        Assert.assertEquals(SafetyActivities4Covid19.Status.DISABLED, record1104.getStatus());
        Assert.assertNull(record1104.getActivity());
        Assert.assertEquals("C-DAGOLL", record1104.getOrganization().getOrganizationId());


        Activity activity1101 = safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.ENABLED,
                DateUtils.createDate("24-10-2021 16:00:00"), "OK: XXX 2").getActivity();
        Assert.assertEquals("ACT-1101", activity1101.getActId());

        Activity activity1102 = safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.ENABLED,
                DateUtils.createDate("24-10-2021 16:00:00"), "OK: XXX 2").getActivity();
        Assert.assertEquals("ACT-1102", activity1102.getActId());

        ////
        // Tickets
        ////
        safetyActivities4Covid19.createTicket("idUser1", "ACT-1105", DateUtils.createLocalDate("24-11-2021"));
        safetyActivities4Covid19.createTicket("idUser2", "ACT-1105", DateUtils.createLocalDate("24-11-2021"));
        safetyActivities4Covid19.createTicket("idUser3", "ACT-1105", DateUtils.createLocalDate("24-11-2021"));

        Order o1 = safetyActivities4Covid19.assignSeat("ACT-1105");
        Iterador<Ticket> it1 = o1.tickets();

        Order o2 = safetyActivities4Covid19.assignSeat("ACT-1105");
        Iterador<Ticket> it2 = o2.tickets();

        Order o3 = safetyActivities4Covid19.assignSeat("ACT-1105");
        Iterador<Ticket> it3 = o3.tickets();

        Ticket t1 = it1.siguiente();
        Ticket t2 = it2.siguiente();
        Ticket t3 = it3.siguiente();

        Assert.assertEquals(1, t1.getSeat());
        Assert.assertEquals("idUser1", t1.getUser().getId());
        Assert.assertEquals(SafetyActivities4Covid19.Badge.SENIOR_PLUS, t1.getUser().getBadge(DateUtils.createLocalDate("24-11-2021")));

        Assert.assertEquals(2, t2.getSeat());
        Assert.assertEquals("idUser2", t2.getUser().getId());
        Assert.assertEquals(SafetyActivities4Covid19.Badge.MASTER, t2.getUser().getBadge(DateUtils.createLocalDate("24-11-2021")));

        Assert.assertEquals(3, t3.getSeat());
        Assert.assertEquals("idUser3", t3.getUser().getId());
        Assert.assertEquals(SafetyActivities4Covid19.Badge.DARK, t3.getUser().getBadge(DateUtils.createLocalDate("23-04-2000")));

        safetyActivities4Covid19.createTicket("idUser4", "ACT-1102", DateUtils.createLocalDate("23-04-2000"));
        safetyActivities4Covid19.createTicket("idUser5", "ACT-1102", DateUtils.createLocalDate("23-04-2000"));
        safetyActivities4Covid19.createTicket("idUser6", "ACT-1102", DateUtils.createLocalDate("23-04-2000"));
        safetyActivities4Covid19.createTicket("idUser7", "ACT-1102", DateUtils.createLocalDate("23-04-2000"));


        Order o4 = safetyActivities4Covid19.assignSeat("ACT-1102");
        Iterador<Ticket> it4 = o4.tickets();

        Order o5 = safetyActivities4Covid19.assignSeat("ACT-1102");
        Iterador<Ticket> it5 = o5.tickets();

        Order o6 = safetyActivities4Covid19.assignSeat("ACT-1102");
        Iterador<Ticket> it6 = o6.tickets();

        Order o7 = safetyActivities4Covid19.assignSeat("ACT-1102");
        Iterador<Ticket> it7 = o7.tickets();

        Ticket t4 = it4.siguiente();
        Ticket t5 = it5.siguiente();
        Ticket t6 = it6.siguiente();
        Ticket t7 = it7.siguiente();

        Assert.assertEquals(1, t4.getSeat());
        Assert.assertEquals(2, t5.getSeat());
        Assert.assertEquals(3, t6.getSeat());
        Assert.assertEquals(4, t7.getSeat());

        ////
        /// Roles
        ////
        safetyActivities4Covid19.addRole("R1", "director");
        safetyActivities4Covid19.addRole("R2", "musician");
        safetyActivities4Covid19.addRole("R3", "sound technician");
        safetyActivities4Covid19.addRole("R4", "choreographer");
        safetyActivities4Covid19.addRole("R5", "author");

        ////
        /// Workers
        ////
        safetyActivities4Covid19.addWorker("W1", "Rafa", "Vidal",
                DateUtils.createLocalDate("23-04-2008"), true, "R1", "C-FURA");

        safetyActivities4Covid19.addWorker("W2", "Anna", "Agustí",
                DateUtils.createLocalDate("23-04-2000"), true, "R2", "C-FURA");

        safetyActivities4Covid19.addWorker("W3", "Sebastià", "Sallent",
                DateUtils.createLocalDate("23-04-1960"), true, "R5", "C-FURA");

        safetyActivities4Covid19.addWorker("W4", "Jesus", "Alcober",
                DateUtils.createLocalDate("23-04-2000"), false, "R3", "C-TRICICLE");

        safetyActivities4Covid19.addWorker("W5", "Juan", "Hdez",
                DateUtils.createLocalDate("23-04-2000"), false, "R1", "C-TRICICLE");

        safetyActivities4Covid19.addWorker("W6", "Quim", "Valls",
                DateUtils.createLocalDate("23-04-1950"), true, "R4", "C-DAGOLL");

        safetyActivities4Covid19.addWorker("W7", "Enric", "Giró",
                DateUtils.createLocalDate("23-04-1955"), true, "R4", "C-DAGOLL");

        ////
        /// Groups
        ////
        safetyActivities4Covid19.addGroup("GX", "description (GX)",
                DateUtils.createLocalDate("24-11-2021"), "idUser8", "idUser10");

        safetyActivities4Covid19.addGroup("G2021", "description (G21)",
                DateUtils.createLocalDate("24-11-2021"), "idUser5", "idUser6", "idUser11", "idUser12", "W6", "W7");



        return safetyActivities4Covid19;
    }



}