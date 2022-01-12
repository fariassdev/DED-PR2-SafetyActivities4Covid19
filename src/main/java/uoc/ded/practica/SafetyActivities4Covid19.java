package uoc.ded.practica;

import java.time.LocalDate;
import java.util.Date;

import uoc.ded.practica.exceptions.*;
import uoc.ded.practica.model.*;
import uoc.ei.tads.Iterador;


/**
 * Definición del TAD de gestión de la plataforma de gestión de actividades culturales
 */
public interface SafetyActivities4Covid19 {

    /**
     * dimensión de las mejores organizaciones
     */
    public static final int BEST_ORGANIZATIONS = 5;

    /**
     * dimensión para el contenedor de las 10 mejores actividades
     */
    public static final int BEST_10_ACTIVITIES = 10;

    /**
     * dimensión para el vector de roles
     */
    public static final int MAX_NUMBER_OF_ROLES = 50;

    /**
     * dimensión del contenedor de usuarios
     */
    public static final  int U = 45;

    /**
     * dimensión del contenedor de organizaciones
     */
    public static final  int O = 25;


    /**
     * dimensión del contenedor de actividades
     */
    public static final  int A = 256;

    /**
     * dimensión del contenedor de roles
     */
    public static final int R = 15;


    enum Mode {
        ON_LINE,
        FACE2FACE
    }

    enum Status {
        PENDING,
        ENABLED,
        DISABLED
    }

    enum Rating {

        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5);

        private final int value;

        private Rating(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum Badge {
        SENIOR_PLUS(100),
        SENIOR(85),
        MASTER_PLUS(75),
        YOUTH_PLUS(65),
        MASTER(50),
        YOUTH(40),
        JUNIOR_PLUS(35),
        JUNIOR(20),
        DARK(0);

        private final int value;

        private Badge(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }



    /**
     * Método que permite añadir un usuario en el sistema
     *
     * @param userId           identificador del usuario
     * @param name             nombre del usuario
     * @param surname          apellidos del usuario
     * @param birthday         fecha de nacimiento
     * @param covidCertificate certificado de covid
     * @pre cierto.
     * @post si el código de usuario es nuevo, los usuarios serán los mismos más
     * un nuevo usuario con los datos indicados. Si no, los datos del
     * usuario se habrán actualizado con los nuevos.
     */
    public void addUser(String userId, String name, String surname, LocalDate birthday,
                        boolean covidCertificate);


    /**
     * Método que añade una organización en el sistema
     *
     * @param organizationId
     * @param name
     * @param description
     * @pre cierto.
     * @post Si el código de organización no existe las organizaciones serán las
     * mismas más una nueva con los datos indicados. Si no, los datos de la organización
     * se habrán actualizado con los nuevos.
     */
    public void addOrganization(String organizationId, String name, String description);


    /**
     * Método que añade un nuevo expediente en el sistema
     *
     * @param recordId       identificador del expediente
     * @param actId          identificador de la actividad cultural
     * @param description    la descripción de la actividad cultural
     * @param date           fecha en la que se realizará la actividad cultural
     * @param dateRecord     fecha en la que se registra el expediente
     * @param mode           tipo de actividad cultural (ON-LINE o PRESENCIAL)
     * @param num            número máximo de asistentes
     * @param organizationId identificador de la organización
     * @pre la actividad y el código de expediente no existen.
     * @post los expedientes serán los mismos
     * más uno nuevo con los datos indicados. El nuevo expediente es el
     * elemento de la cima de expedientes.
     * En caso que la organización, identificada por organizacionId, no exista,
     * se deberá informar del error
     */
    public void addRecord(String recordId, String actId, String description, Date date, LocalDate dateRecord,
                          Mode mode, int num, String organizationId) throws OrganizationNotFoundException;


    /**
     * Método que actualiza el estado del expediente pendiente de validar y retorna
     * el expediente
     *
     * @param status      estado del espediente
     * @param date        fecha en la que se realiza la valoración del expediente
     * @param description descripción de la valoración del expediente
     * @return Retorna el expediente modificado
     * @throws NoRecordsException lanza una excepción en caso que no existan expedientes pendientes de valorar
     * @pre cierto.
     * @post El estado del elemento que está en la cima de la pila de expedientes
     * se modifica, el número
     * de expedientes serán los mismos menos uno (la cima) y el número de actividades
     * serán los mismos más uno, en caso que el expediente sea favorable. En caso que
     * no haya expedientes en la pila, se deberá informar del error.
     */
    public Record updateRecord(Status status, Date date, String description) throws NoRecordsException;


    /**
     * Método que permite comprar entradas en una actividad cultural
     *
     * @param userId identificador del usuario
     * @param actId  identificador de la actividad cultural
     * @param date data en la que es fa la compra d'entrades
     * @throws UserNotFoundException     lanza la excepción en caso que el usuario no exista
     * @throws ActivityNotFoundException lanza la excepción en caso que la actividad no exista
     * @throws LimitExceededException    lanza la excepción en caso que se pidan más entradas que las disponibles
     * @pre cierto.
     * @post El número de órdenes de compra de una actividad cultural serán los mismos más una unidad.
     * En caso que el usuario o la actividad cultural no exista, se deberá informar de
     * un error. En caso que ya se haya superado el máximo de plazas, se deberá indicar un error.
     */
    public Order createTicket(String userId, String actId, LocalDate date) throws UserNotFoundException,
            ActivityNotFoundException, LimitExceededException;


    /**
     * Método que permite la asignación de un asiento en un acto cultural
     *
     * @param actId identificador de la actividad
     * @return retorna la orden de compra con las entradas asignadas
     * @throws ActivityNotFoundException lanza la actividad en caso que no exista
     * @pre cierto.
     * @post La cabeza de la cola indica el asiento a asignar y el número de tickets pendientes
     * de asignar de una actividad cultural serán los mismos menos una unidad.  En caso que
     * la actividad cultural no exista, se deberá informar de un error.
     */
    public Order assignSeat(String actId) throws ActivityNotFoundException;


    /**
     * Método que añade una valoración sobre una actividad cultural por parte de un usuario
     *
     * @param actId   identificador de la actividad
     * @param rating  valoración de la actividad
     * @param message mensaje asociado a la actividad
     * @param userId  identificador de la actividad
     * @throws ActivityNotFoundException  se lanza la excepción en caso que la actiivdad no exista
     * @throws UserNotFoundException      se lanza la excepción en caso que el usuario no exista
     * @throws UserNotInActivityException se lanza la excepción en caso que el usuario no
     *                                    haya participado en la actividad cultural
     * @pre cierto.
     * @post las valoraciones serán las mismas más una nueva con los datos indicados. En caso que
     * la actividad o el usuario no exista, se deberá informar del
     * error. Si el usuario no ha participado en la actividad cultural también se
     * deberá informar del error.
     */
    public void addRating(String actId, Rating rating, String message, String userId)
            throws ActivityNotFoundException, UserNotFoundException, UserNotInActivityException;


    /**
     * Método que proporciona las valoraciones de una actividad cultural
     *
     * @param actId identificador de la actividad
     * @return retorna un iterador para recorrer las actividades culturales
     * @throws ActivityNotFoundException se lanza la excepción en caso que no exista la actividad
     * @throws NoRatingsException        se lanza la excepción en caso que no existan valoraciones sobre la actividad
     * @pre cierto.
     * @post devuelve un iterador para recorer las valoraciones de una actividad. En caso
     * de que no exista la actividad o no haya valoraciones se indicará un error
     */
    public Iterador<uoc.ded.practica.model.Rating> getRatings(String actId) throws ActivityNotFoundException, NoRatingsException;


    /**
     * Método que proporciona la actividad mejor valorada
     *
     * @return retorna la actividad mejor valorada
     * @throws NoActivitiesException se lanza la excepción en caso que no exista ninguna actividad
     * @pre cierto.
     * @post devuelve la actividad mejor valorada. En caso que no exista
     * ninguna actividad se deberá indicar un error
     */
    public Activity bestActivity() throws ActivityNotFoundException;


    /**
     * Método que proporcina el usuario más participativo
     *
     * @return retorna el usuario más participativo
     * @throws UserNotFoundException se lanza la excepción en caso que no exista ningún usuario com actividad
     * @pre cierto.
     * @post devuelve al usuario más activo (mayor participación en actividades culturales).
     * En caso que exista más de un usuario con el mismo número de participaciones se proporciona aquel que participó con anterioridad. En caso que no exista ningún usuario se deberá indicar un error
     * mostActiveUser(): User
     */
    public User mostActiveUser() throws UserNotFoundException;


    /**
     * Método que proporciona el % de expedientes rechazados
     *
     * @return retorna el % de expedientes rechazados
     * @pre cierto.
     * @post devuelve un entero con el valor de expedientes que no han sido validados
     */
    public double getInfoRejectedRecords();


    /**
     * Método que proporciona todas las actividades del sistema
     *
     * @return retorna un iterador para recorrerr todas las actividades
     * @throws NoActivitiesException lanza una excepción en caso que no hayan actividades
     * @pre cierto.
     * @post devuelve un iterador para recorrer todas las actividades. En caso
     * que no existan actividades se deberá indicar un error
     */
    public Iterador<Activity> getAllActivities() throws NoActivitiesException;




    /**
     * Método que proporciona las actividades a las que ha assistido un usuario
     *
     * @pre cierto.
     * @post devuelve un iterador para recorrer todos los usuarios que han participado en una actividad cultural. En caso que no
     * existan usuarios o la actividad no exista,  se deberá indicar un error
     *
     * @param userId identificador del usuario
     * @return retorna un iterador para recorrer las actividades de un usuario
     * @throws NoActivitiesException lanza una excepción en caso que no haya actividades asociadas al usuario
     * @pre el usuario existe.
     * @post devuelve un iterador para recorrer las actividades de un usuario. En caso que no existan actividades se indicará un error
     */
    public Iterador<Activity> getActivitiesByUser(String userId) throws NoActivitiesException;











    ///////////////////////////////////////////////////////////////////////
    ///
    ///////////////////////////////////////////////////////////////////////


    /**
     * Método que proporciona el usuario identificado
     *
     * @param userId identificador del usuario
     * @return retorna el usuario o null en caso que no exista
     */
    public User getUser(String userId);


    /**
     * Método que proporciona una organización
     *
     * @param organizationId identificador de la organización
     * @return retorna la organización o null en caso que no exista.
     */
    public Organization getOrganization(String organizationId);

    /**
     * Método que proporciona el expediente actual a valorar
     *
     * @return retorna el  expediente a valorar o null si no hay ninguno
     */
    public Record currentRecord();

    /**
     * Método que proporciona el número de usuarios
     *
     * @return retona el número de usuarios
     */
    public int numUsers();

    /**
     * Método que proporciona el número de organizaciones
     *
     * @return retona el número de organizaciones
     */
    public int numOrganizations();


    /**
     * Método que retorna el número de expedientes pendientes de validar
     *
     * @return retorna el número de expedientes
     */
    public int numPendingRecords();

    /**
     * Método que proporciona el total de expedientes que hay en el sistema
     *
     * @return retorna el número total de expedientes
     */
    public int numRecords();

    /**
     * Método que proporciona el número de expedientes que han sido rechazados
     *
     * @return retorna el número de expedientes rechazados
     */
    public int numRejectedRecords();

    /**
     * Método que proporciona el número de actividades
     *
     * @return retorna el número de actividades
     */
    public int numActivities();


    /**
     * Método que proporciona el número de actividades de una organización
     *
     * @param organizationId identificador de la actividad
     * @return retorna el número de actividades de la organización
     * @PRE la organización existe
     */
    public int numActivitiesByOrganization(String organizationId);

    /**
     * Método que proporciona el número total de expedientes de una organización
     *
     * @param organizationId identificador de la actividad
     * @return retorna el número de expedientes de la organización
     * @PRE la organización existe
     */
    public int numRecordsByOrganization(String organizationId);

    /**
     * Método que proporciona una actividad
     *
     * @param actId identificador de la actividad
     * @return retorna la actividad o null si no existe
     */
    public Activity getActivity(String actId);


    /**
     * Método que proporciona el número de entradas disponibles sobre una actividad cultural
     *
     * @param actId identificador de la actividad cultural
     * @return retorna el número de entradas disponibles de una actividad cultural o cero en cualquier otro caso
     */
    public int availabilityOfTickets(String actId);


    ///
    // PR2
    //


    /**
     * (1) Método que permite añadir o modificar roles en el sistema
     *
     * @pre cierto.
     * @post si el código de rol es nuevo, los roles serán los mismos más un rol nuevo
     * con los datos indicados. Si no, los datos del rol se habrán actualizado con los nuevos.
     *
     * @param roleId identificador de rol
     * @param name nombre del rol
     */
    public void addRole(String roleId, String name);


    /**
     * (2) Método que permite añadir un trabajador en el sistema
     *
     * @pre cierto.
     * @post si el código de usuario es nuevo, los usuarios serán los mismos más un nuevo usuario
     * y el número usuarios de una organización serán los mismos más uno nuevo, con los datos
     * indicados y el número de usuario de un rol serán los mismos más uno nuevo. Si no, los datos
     * del usuario se habrán actualizado con los nuevos;
     *
     * addWorker(userID, name, surname, birthday,  covidCertificate, role)
     * @param userId identificador del trabajador
     * @param name nombre del trabajador
     * @param surname apellidos del trabajador
     * @param birthday fecha de nacimiento del trabajador
     * @param covidCertificate certificado covid
     * @param roleId identificador del rol del trabajador
     * @param organizationId organización a la que pertenece el trabajador
     */
    public void addWorker(String userId, String name, String surname, LocalDate birthday,
                          boolean covidCertificate, String roleId, String organizationId);


    /**
     * (3) Método que proporciona los trabajadores de una organización
     *
     * @pre cierto.
     * @post devuelve un iterador para recorrer todos trabajadores de una orrganización. En caso
     * que no existan trabajadores o la organización no exista,  se deberá indicar un error
     *
     * @param organizationId identificador de la organización
     * @return retorna un iterador para recorrer los trabajadores de la organización
     * @throws OrganizationNotFoundException se lanza la excepción en caso que la organización no exista
     * @throws NoWorkersException se lanza la excepción en caso que no existan trabajadores
     */
    public Iterador<Worker> getWorkersByOrganization(String organizationId) throws OrganizationNotFoundException, NoWorkersException;


    /**
     * (4) Método que proporciona los usuarios que han asistido a una actividad cultural
     *
     * @pre cierto.
     * @post devuelve un iterador para recorrer todos los usuarios que han participado
     * en una actividad cultural. En caso que no existan usuarios o la actividad
     * no exista,  se deberá indicar un error
     *
     * @param activityId identificador de la actividad cultural
     * @return retorna un iterador para recorrer los usuarios que han participado en una actividad cultural
     *
     * @throws ActivityNotFoundException se lanza en caso que la actividad no exista
     * @throws NoUserException se lanza en caso que no hayan usuarios
     */
    public Iterador<User> getUsersInActivity(String activityId) throws ActivityNotFoundException, NoUserException;


    /**
     * (5) Método que proporciona la insignia asociada a un usuario
     *
     * @pre cierto.
     * @post devuelve la insignia asociada al usuario. En caso que no exista el usuario,  se deberá indicar un error
     *
     * @param userId identificador del usuario
     * @param day dia en el que se consulta la insignia
     * @return retorna la insignia asociada al usuario
     * @throws UserNotFoundException lanza la excepción en caso que el usuario no exista
     */
    public Badge getBadge(String userId, LocalDate day) throws UserNotFoundException;

    /**
     * (6) Método que permite crear un grupo y asignar un conjunto de miembros
     *
     * @pre todos los miembros del grupo existen
     * @post si el código de grupo es nuevo, los grupos serán los mismos más un grupo nuevo con los datos
     * indicados. Si no, los datos del grupo se habrán actualizado con los nuevos.
     *
     * @param groupId identificador de grupo
     * @param description descripción del grupo
     * @param date fecha de creación del grupo
     * @param members miembros del grupo
     */
    public void addGroup(String groupId, String description, LocalDate date, String... members );

    /**
     * (7) Método que proporciona un iterador para recorrer los miembros de un grupo
     *
     * @pre cierto.
     * @post devuelve un iterador para recorrer todos los usuarios de un grupol. En caso
     * que no existan el grupo o no haya usuarios,  se deberá indicar un error
     *
     * @param groupId identificador del grupo
     * @return retorna un iterador para recorrer los miembros de un grupo
     * @throws GroupNotFoundException lanza la excepción si el grupo no existe
     * @throws NoUserException lanza la excepción en caso que el grupo no tenga miembros
     */
    public Iterador<User> membersOf(String groupId) throws GroupNotFoundException, NoUserException;

    /**
     * (8) Método que calcula el valor medio de un grupo.
     *
     * @pre cierto.
     * @post devuelve el valor medio de un grupo formado por el valor de los miembros del
     * grupo (insignias).  En caso
     * que no exista el grupo,  se deberá indicar un error
     *
     * @param groupId identificador del grupo
     * @return retorna el valor medio de un grupo
     * @throws GroupNotFoundException se lanza la excepción en caso que el grupo no exista
     */
    public double valueOf(String groupId) throws GroupNotFoundException;

    /**
     * (9) Método que permite comprar entradas a un grupo creado previamente
     *
     * @param groupId identificador del grupo
     * @param actId identificador de la actividad
     * @param date fecha en la que se realiza la compra
     * @return retorna la órden de compra asociada a la compra de las entradas
     * @throws GroupNotFoundException se lanza la excepción en caso que el grupo no exista
     * @throws ActivityNotFoundException se lanza la excepción en caso que la actividad no exista
     * @throws LimitExceededException se lanza la excepción en caso que se exceda el número de entradas
     */
    public Order createTicketByGroup(String groupId, String actId, LocalDate date) throws GroupNotFoundException,
            ActivityNotFoundException, LimitExceededException;


    /**
     * (10) Método que proporciona información de una orden de compra
     *
     * @pre cierto.
     * @post devuelve la orden asociada con el identificador. En caso que no exista, se indicará un error.
     *
     * @param orderId identificador de la orden de compra
     *
     * @return retorna la orden de compra
     * @throws OrderNotFoundException se lanza la excepción en caso que la orden de compra no exista
     */
    public Order getOrder (String orderId) throws OrderNotFoundException;


    /**
     * (11) Método que proporciona un iterador con los trabajadores de un rol
     *
     * @pre el rol existe
     * @post devuelve un iterador con los trabajadores que tengan asociado un rol. En caso que no existan
     * trabajadores, se indicará un error.
     *
     * @param roleId identificador del rol
     * @return retorna un iterador con los trabajadores asociados a un rol
     * @throws NoWorkersException se lanza la excepción en caso que no existan trabajadores
     */
    public Iterador<Worker> getWorkersByRole(String roleId) throws NoWorkersException;

    /**
     * (12a) Método que proporciona un iterador con las actividades creadas por la organización
     *
     * @param organizationId identificador de la organización
     * @return retorna un iterador con las actividades de una organización
     * @throws NoActivitiesException lanza una excepción en caso que no existan actividades
     *                               creadas por la organización
     * @pre la organitzación existe.
     * @post retorna un iterador para recorrer las actividades de una organización. En caso que no existan
     *
     * las actividades se indicará un error
     */
    public Iterador<Activity> getActivitiesByOrganization(String organizationId) throws NoActivitiesException;

    /**
     * (12b) Método que proporciona un iterador con los expedientes creadoss por la organización
     *
     * @param organizationId identificador de la organización
     * @return retorna un iterador con los expedientes de una organización
     * @throws NoRecordsException lanza una excepción en caso que no existan expedientes
     *                               creadas por la organización
     * @pre la organitzación existe.
     * @post retorna un iterador para recorrer los expedientes de una organización. En caso que no existan
     *
     * expedientes se indicará un error
     */
    public Iterador<Record> getRecordsByOrganization(String organizationId) throws NoRecordsException;

    /**
     * (13) Método que proporciona las 5 (o menos) mejores organizaciones
     *
     * @pre cierto.
     * @post retorna un iterador para recorrer las 5 mejores organizaciones
     *
     * @return retorna un iterador con las 5 (o menos) mejores organizaciones
     * @throws NoOrganizationException
     */
    public Iterador<Organization> best5Organizations() throws NoOrganizationException;

    /**
     * Método que proporciona las 10 mejor actividades según su valoración
     *
     * @return devuelve un iterador con las 10 mejores actividades
     * @throws NoActivitiesException se lanza la excepción en caso de que no exista ninguna actividad
     * @pre cierto.
     * @tabla devuelve un iterador con las 10 mejores actividades. En caso de que no exista
     * ninguna actividad se tendrá que indicar un error
     */
    public Iterador<Activity> best10Activities() throws ActivityNotFoundException;


    /**
     * Método que proporciona el trabajador identificado
     * @param workerId identificador del trabajador
     * @return retorna el trabajador requerido o null en caso que no exista
     */
    public Worker getWorker(String workerId);


    /**
     * Método que proporciona el rol
     * @param roleId identificador del rol
     * @return retorna el rol requerido o null en caso que no exista
     */
    public Role getRole(String roleId);

    /**
     * Método que proporciona el grupo
     * @param groupId identificador del grupo
     * @return retorna el grupo requerido o null en caso que no exista
     */
    public Group getGroup(String groupId);


    /**
     * Método que proporciona el número de trabajadores que hay en el sistema
     *
     * @return retorna el número de trabajadores en el sistema
     */
    public int numWorkers();

    /**
     * Método que proporciona el número de trabajadores de una organización
     *
     * @param organizationId identificador de la organización
     *
     * @return retorna el número de trabajadores de una organización o cero en caso que no existan
     *
     */
    public int numWorkers(String organizationId);

    /**
     * Método que proporciona el número de roles
     * @return retorna el número de roles
     */
    public int numRoles();


    /**
     * Método que proporciona el número de trabajadores asociados a un rol
     * @param roleId identificador del rol
     *
     * @return retorna el número de trabajadores o cero en caso que no existan
     */
    public int numWorkersByRole(String roleId);

    /**
     * Método que proporciona el número de grupos que existen en el sistema
     * @return retorna el número de grupos
     */
    public int numGroups();


    /**
     * Método que proporciona el número de órdenes de compra
     * @return retorna el número de órdenes de compra
     */
    public int numOrders();


}

