package uoc.ded.practica.exceptions;

/**
 * Excepción específica.
 *
 * @author UOC
 * @version Otoño 2021
 */
public class DEDException extends Exception {

    private static final long serialVersionUID = -2577150645305791318L;

    public DEDException() {
        super();
    }

    public DEDException(String msg) {
        super(msg);
    }

}
