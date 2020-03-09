package ru.job4j.servlets.crud.persistent.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.servlets.crud.logic.user.Validate;
import ru.job4j.servlets.crud.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbUserStore implements Validate {
    private static final Logger LOG = LogManager.getLogger(DbUserStore.class.getName());
    private static final DbUserStore INSTANCE = new DbUserStore();
    private final BaseCommonPool baseCommonPool = BaseCommonPool.getInstance();
    public static final String PARAMETRIZED_QUERY_WITH_PRIMARY_KEY = "user_id=?";
    public static final String TABLE_NAME = "users";

    private DbUserStore() {

    }

    public static DbUserStore getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(User user) {
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (name, login, email, image, password, role_id, date, country, city) values (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            setCommonStatement(user, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOG.error("При создании пользователя появилась ошибка", e);
        }
    }

    @Override
    public void update(User user) {
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("UPDATE " + TABLE_NAME + " SET name=?, login=?, email=?, image=?, password=?, role_id=?, date=?, country=?, city=? WHERE "
                             + PARAMETRIZED_QUERY_WITH_PRIMARY_KEY)) {
            setCommonStatement(user, preparedStatement);
            preparedStatement.setInt(10, user.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOG.error("Ошибка в методе обновления пользователя " + user, e);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE "
                             + PARAMETRIZED_QUERY_WITH_PRIMARY_KEY)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOG.error("Ошибка удаление пользователя с id: " + id, e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> listOfFoundUsers = new ArrayList<>();
        try (Connection connection = baseCommonPool.getConnect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT u.user_id, u.name, u.login, u.email, "
                     + "u.image, u.password, u.role_id, u.date, r.name, u.country, u.city FROM users AS u INNER JOIN role AS r ON u.role_id = r.role_id")) {
            while (resultSet.next()) {
                listOfFoundUsers.add(fillFoundUser(resultSet));
            }
        } catch (Exception e) {
            LOG.error("Смотри запрос выборки с двух таблиц", e);
        }
        return listOfFoundUsers;
    }

    @Override
    public User findById(int id) {
        User foundUser = null;
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement prepare = connection.prepareStatement("SELECT u.user_id, u.name, u.login, u.email, "
                     + " u.image, u.password, u.role_id, u.date, r.name, u.country, u.city"
                     + " FROM users AS u INNER JOIN role AS r ON u.role_id = r.role_id WHERE "
                     + PARAMETRIZED_QUERY_WITH_PRIMARY_KEY)) {
            prepare.setInt(1, id);
            ResultSet resultSet = prepare.executeQuery();
            if (resultSet.next()) {
                foundUser = fillFoundUser(resultSet);
            }
        } catch (Exception e) {
            LOG.error("Ошибка поиск пользователя с id: " + id, e);
        }
        return foundUser;
    }

    @Override
    public boolean isUserExist(String requestLogin, String requestPassword) {
        boolean userExists = false;
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement prepare = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE login=? AND password=?")) {
            prepare.setString(1, requestLogin);
            prepare.setString(2, requestPassword);
            ResultSet resultSet = prepare.executeQuery();
            if (resultSet.next()) {
                String login = resultSet.getString(3);
                String password = resultSet.getString(6);
                userExists = login != null && password != null;
            }
        } catch (Exception e) {
            LOG.error("Проверка на существование пароля и логина", e);
        }
        return userExists;
    }

    @Override
    public User findByLoginAndPassword(String requestLogin, String requestPassword) {
        User foundUser = null;
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement prepare = connection.prepareStatement("SELECT u.user_id, u.name, u.login, u.email,"
                     + " u.image, u.password, u.role_id, u.date, r.name, u.country, u.city"
                     + " FROM users AS u INNER JOIN role AS r ON u.role_id = r.role_id WHERE login=? AND password=?")) {
            prepare.setString(1, requestLogin);
            prepare.setString(2, requestPassword);
            ResultSet resultSet = prepare.executeQuery();
            if (resultSet.next()) {
                foundUser = fillFoundUser(resultSet);
            }
        } catch (Exception e) {
            LOG.error("Ошибка поиск пользователя по логину: " + requestLogin, e);
        }
        return foundUser;
    }

    private User fillFoundUser(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String login = resultSet.getString(3);
        String email = resultSet.getString(4);
        String image = resultSet.getString(5);
        String password = resultSet.getString(6);
        int roleId = resultSet.getInt(7);
        long date = resultSet.getDate(8).getTime();
        String roleName = resultSet.getString(9);
        String country = resultSet.getString(10);
        String city = resultSet.getString(11);
        return new User(userId, name, login, email, image, date, password, roleId, roleName, country, city);
    }

    private void setCommonStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getLogin());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setString(4, user.getPhotoId());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setInt(6, user.getRoleId());
        preparedStatement.setDate(7, new Date(user.getCreateDate()));
        preparedStatement.setString(8, user.getCountry());
        preparedStatement.setString(9, user.getCity());
    }
}
