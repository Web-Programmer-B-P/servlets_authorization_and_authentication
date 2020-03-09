package ru.job4j.servlets.crud.logic.countries;

import java.util.Map;

public interface CityStorage {
    Map<Integer, String> getAllCountry();
    Map<Integer, String> getAllRegion(int countryId);
    Map<Integer, String> getAllCity(int regionId);
}
