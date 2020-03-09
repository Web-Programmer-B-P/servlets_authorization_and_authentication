package ru.job4j.servlets.crud.logic.role;

import ru.job4j.servlets.crud.model.Role;
import ru.job4j.servlets.crud.persistent.db.DbRoleStore;

import java.util.List;

public class ValidateRole implements RoleStore {
    private final RoleStore logicRole = DbRoleStore.getInstance();
    private static final ValidateRole INSTANCE = new ValidateRole();

    private ValidateRole() {

    }

    public static ValidateRole getInstance() {
        return INSTANCE;
    }

    @Override
    public void addRole(Role role) {
        logicRole.addRole(role);
    }

    @Override
    public List<Role> findAllRoles() {
        return logicRole.findAllRoles();
    }

    @Override
    public void updateRole(Role role) {
        logicRole.updateRole(role);
    }

    @Override
    public void deleteRole(int idRole) {
        logicRole.deleteRole(idRole);
    }

    @Override
    public Role findRoleById(int idRole) {
        return logicRole.findRoleById(idRole);
    }
}
