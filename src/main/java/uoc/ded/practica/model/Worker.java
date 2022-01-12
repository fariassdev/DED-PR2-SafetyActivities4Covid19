package uoc.ded.practica.model;

import java.time.LocalDate;

public class Worker extends User {

    private Role role;

	public Worker(String idUser, String name, String surname, LocalDate birthday, boolean covidCertificate) {
        super(idUser, name, surname, birthday, covidCertificate);
    }

    public String getRoleId() {
	    return this.role.getRoleId();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
