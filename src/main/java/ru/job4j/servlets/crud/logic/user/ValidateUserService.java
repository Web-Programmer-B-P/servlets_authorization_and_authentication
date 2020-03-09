package ru.job4j.servlets.crud.logic.user;

import ru.job4j.servlets.crud.model.User;
import ru.job4j.servlets.crud.persistent.db.DbUserStore;

import java.util.List;

public class ValidateUserService implements Validate {
    private static final ValidateUserService INSTANCE = new ValidateUserService();
    private final Validate logic = DbUserStore.getInstance();

    private ValidateUserService() {

    }

    public static ValidateUserService getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(User user) {
        if (findById(user.getId()) == null) {
            logic.add(user);
        }
    }

    @Override
    public void update(User user) {
        logic.update(user);
    }

    @Override
    public void delete(int id) {
        logic.delete(id);
    }

    @Override
    public List<User> findAll() {
        return logic.findAll();
    }

    @Override
    public User findById(int id) {
        return logic.findById(id);
    }

    @Override
    public boolean isUserExist(String requestLogin, String requestPassword) {
        return logic.isUserExist(requestLogin, requestPassword);
    }

    @Override
    public User findByLoginAndPassword(String requestLogin, String requestPassword) {
        return logic.findByLoginAndPassword(requestLogin, requestPassword);
    }
}
