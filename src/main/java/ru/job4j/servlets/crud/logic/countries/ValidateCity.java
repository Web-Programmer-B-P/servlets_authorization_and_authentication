package ru.job4j.servlets.crud.logic.countries;

import ru.job4j.servlets.crud.persistent.db.DbCityStore;

import java.util.Map;

public class ValidateCity implements CityStorage {
    private static final ValidateCity INSTANCE_VALIDATE_CITY = new ValidateCity();
    private final DbCityStore dbCityStore = DbCityStore.getInstance();

    private ValidateCity() {

    }

    public static ValidateCity getInstance() {
        return INSTANCE_VALIDATE_CITY;
    }
    @Override
    public Map<Integer, String> getAllCountry() {
        return dbCityStore.getAllCountry();
    }

    @Override
    public Map<Integer, String> getAllRegion(int countryId) {
        return dbCityStore.getAllRegion(countryId);
    }

    @Override
    public Map<Integer, String> getAllCity(int regionId) {
        return dbCityStore.getAllCity(regionId);
    }
}
