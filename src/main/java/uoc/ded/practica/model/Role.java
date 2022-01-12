package uoc.ded.practica.model;

public class Role {
    private String roleId;
    private String name;

    public Role(String roleId, String name) {
        this.roleId = roleId;
        this.name = name;
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
}
