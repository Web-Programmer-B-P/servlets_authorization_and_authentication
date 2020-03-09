package ru.job4j.servlets.crud.logic.user;

import ru.job4j.servlets.crud.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidateStub implements Validate {
    private final Map<Integer, User> store = new HashMap<>();
    private int ids = 0;

    @Override
    public void add(User user) {
        user.setId(++ids);
        store.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        store.replace(user.getId(), user);
    }

    @Override
    public void delete(int id) {
        store.remove(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public User findById(int id) {
        return store.get(id);
    }

    @Override
    public boolean isUserExist(String requestLogin, String requestPassword) {
        return false;
    }

    @Override
    public User findByLoginAndPassword(String requestLogin, String requestPassword) {
        return null;
    }
}
