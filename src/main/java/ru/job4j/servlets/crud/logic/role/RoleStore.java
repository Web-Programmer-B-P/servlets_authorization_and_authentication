package ru.job4j.servlets.crud.logic.role;

import ru.job4j.servlets.crud.model.Role;

import java.util.List;

public interface RoleStore {
    void addRole(Role role);

    List<Role> findAllRoles();

    void updateRole(Role role);

    void deleteRole(int idRole);

    Role findRoleById(int idRole);
}
