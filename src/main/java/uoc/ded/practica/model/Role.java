package uoc.ded.practica.model;

import uoc.ei.tads.Iterador;
import uoc.ei.tads.ListaEncadenada;
import uoc.ei.tads.Posicion;
import uoc.ei.tads.Recorrido;

public class Role {
    private String roleId;
    private String name;
    private ListaEncadenada<Worker> workers;

    public Role(String roleId, String name) {
        this.roleId = roleId;
        this.name = name;
        workers = new ListaEncadenada<Worker>();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean is(String roleId) {
        return this.roleId.equals(roleId);
    }

    public Iterador<Worker> workers() {
        return workers.elementos();
    }

    public Recorrido<Worker> workerPositions() {
        return workers.posiciones();
    }

    public int numWorkers() {
        return workers.numElems();
    }

    public boolean hasWorkers() {
        return workers.numElems() > 0;
    }

    public void addWorker( Worker user) {
        workers.insertarAlFinal(user);
    }

    public void deleteWorker( String workerId ) {
        for ( Recorrido<Worker> it = this.workerPositions(); it.haySiguiente(); ) {
            final Posicion<Worker> workerPosition = it.siguiente();
            Worker worker = workerPosition.getElem();
            if ( worker.getId().equals( workerId ) ) {
                workers.borrar( workerPosition );
            }

        }
    }
}
