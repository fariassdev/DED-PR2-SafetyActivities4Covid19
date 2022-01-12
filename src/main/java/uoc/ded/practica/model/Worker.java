package uoc.ded.practica.model;

import java.time.LocalDate;

public class Worker extends User {

    private Role role;
    private Organization organization;

	public Worker(String idUser, String name, String surname, LocalDate birthday, boolean covidCertificate, Role role, Organization organization) {
        super(idUser, name, surname, birthday, covidCertificate);
        this.setRole(role);
        this.setOrganization(organization);
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
