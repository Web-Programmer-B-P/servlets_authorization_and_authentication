package ru.job4j.servlets.crud.persistent.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.servlets.crud.logic.role.RoleStore;
import ru.job4j.servlets.crud.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DbRoleStore implements RoleStore {
    private static final Logger LOG = LogManager.getLogger(DbUserStore.class.getName());
    private static final DbRoleStore INSTANCE = new DbRoleStore();
    private final BaseCommonPool baseCommonPool = BaseCommonPool.getInstance();
    private static final String PREPARED_STATEMENT = "role_id=?";
    private static final String TABLE_NAME = "role";

    private DbRoleStore() {

    }

    public static DbRoleStore getInstance() {
        return INSTANCE;
    }

    @Override
    public void addRole(Role role) {
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (name) values (?)")) {
            preparedStatement.setString(1, role.getRole());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOG.error("Ошибка при добавлении новой роли " + role, e);
        }
    }

    @Override
    public void updateRole(Role role) {
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("UPDATE " + TABLE_NAME + " SET name=? WHERE "
                             + PREPARED_STATEMENT)) {
            preparedStatement.setString(1, role.getRole());
            preparedStatement.setInt(2, role.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOG.error("Ошибка в методе обновления роли " + role, e);
        }
    }

    @Override
    public Role findRoleById(int idRole) {
        Role foundRole = null;
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement prepare = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE "
                     + PREPARED_STATEMENT)) {
            prepare.setInt(1, idRole);
            ResultSet resultSet = prepare.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                foundRole = new Role(id, name);
            }
        } catch (Exception e) {
            LOG.error("Ошибка поиск роли с id: " + idRole, e);
        }
        return foundRole;
    }

    @Override
    public void deleteRole(int idRole) {
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE "
                             + PREPARED_STATEMENT)) {
            preparedStatement.setInt(1, idRole);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOG.error("Ошибка удаление роли с id: " + idRole, e);
        }
    }

    @Override
    public List<Role> findAllRoles() {
        List<Role> foundRoles = new ArrayList<>();
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement prepare = connection.prepareStatement("SELECT * FROM " + TABLE_NAME)) {
            ResultSet resultSet = prepare.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                foundRoles.add(new Role(id, name));
            }
        } catch (Exception e) {
            LOG.error("Что то не так с выборкой ролей", e);
        }
        return foundRoles;
    }
}
