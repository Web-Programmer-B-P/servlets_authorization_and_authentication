package ru.job4j.servlets.crud.model;

import java.util.Objects;

public class User {
    private int id;
    private String name;
    private String login;
    private String email;
    private String photoId;
    private String password;
    private long createDate;
    private int roleId;
    private String roleName;
    private String country;
    private String city;

    public User() {

    }

    public User(int id, String name, String login, String email, String photoId, long createDate, String password, int roleId) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
        this.photoId = photoId;
        this.createDate = createDate;
        this.password = password;
        this.roleId = roleId;
    }

    public User(int id, String name, String login, String email) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
    }

    public User(int id, String name, String login, String email, String photoId, long createDate, String password,
                int roleId, String roleName, String country, String city) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
        this.photoId = photoId;
        this.createDate = createDate;
        this.password = password;
        this.roleId = roleId;
        this.roleName = roleName;
        this.country = country;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id
                && createDate == user.createDate
                && Objects.equals(name, user.name)
                && Objects.equals(login, user.login)
                && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, login, email, createDate);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id
                + ", name='" + name + '\''
                + ", login='" + login + '\''
                + ", email='" + email + '\''
                + ", photoId='" + photoId + '\''
                + ", password='" + password + '\''
                + ", createDate=" + createDate
                + ", roleId=" + roleId
                + ", roleName='" + roleName + '\''
                + ", country='" + country + '\''
                + ", city='" + city + '\'' + '}';
    }
}
