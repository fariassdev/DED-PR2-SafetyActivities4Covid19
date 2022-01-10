package uoc.ded.practica.model;

import java.time.LocalDate;

public class Worker extends User {

	public Worker(String idUser, String name, String surname, LocalDate birthday, boolean covidCertificate) {
        super(idUser, name, surname, birthday, covidCertificate);
    }
}
