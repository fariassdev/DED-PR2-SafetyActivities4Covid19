package uoc.ded.practica.model;

import java.time.LocalDate;
import java.util.Comparator;

import uoc.ei.tads.Iterador;
import uoc.ei.tads.Lista;
import uoc.ei.tads.ListaEncadenada;

public class User implements Comparable<User>{
    public static final Comparator<String> CMP = new Comparator<String>() {
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    };

    private String id;
    private String name;
    private String surname;
    private Lista<Activity> activities;
    private LocalDate birthday; // No necesitamos la hora de nacimiento, LocalDate no lleva hora
    private boolean covidCertificate;

	public User(String idUser, String name, String surname, LocalDate birthday, boolean covidCertificate) {
        this.setId(idUser);
        this.setName(name);
        this.setSurname(surname);
        this.setBirthday(birthday);
        this.setCovidCertificate(covidCertificate);
        this.activities = new ListaEncadenada<Activity>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public boolean isCovidCertificate() {
		return covidCertificate;
	}

    public int compareTo(User o) {
        return getId().compareTo(o.getId());
    }

    public Iterador<Activity> answers() {
        return activities.elementos();
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setCovidCertificate(boolean covidCertificate) {
        this.covidCertificate = covidCertificate;
    }

    public boolean is(String userId) {
        return id.equals(userId);
    }

    public Iterador<Activity> activities() {
        return activities.elementos();
    }

    public void addActivity(Activity activity) {
        activities.insertarAlFinal(activity);
    }

    public int numActivities() {
        return activities.numElems();
    }

    public boolean isInActivity(String actId) {
        Iterador<Activity> it = activities.elementos();

        boolean found = false;
        Activity act = null;

        while (!found && it.haySiguiente()) {
        	act = it.siguiente();
            found = act.is(actId);
        }

        return found;
    }

    public boolean hasActivities() {
        return activities.numElems() > 0;
    }
}
