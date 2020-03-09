package ru.job4j.servlets.crud.persistent.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.servlets.crud.logic.countries.CityStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class DbCityStore implements CityStorage {
    private static final DbCityStore INSTANCE = new DbCityStore();
    private final BaseCommonPool baseCommonPool = BaseCommonPool.getInstance();
    private static final Logger LOG = LogManager.getLogger(DbCityStore.class.getName());
    private static final String COUNTRY_TABLE = "country";
    private static final String REGION_TABLE = "region";

    private DbCityStore() {

    }

    public static DbCityStore getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<Integer, String> getAllCountry() {
        Map<Integer, String> allCountry = new HashMap<>();
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement prepare = connection.prepareStatement("SELECT id, name FROM " + COUNTRY_TABLE)) {
            ResultSet resultSet = prepare.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String nameCountry = resultSet.getString(2);
                allCountry.put(id, nameCountry);
            }
        } catch (Exception e) {
            LOG.error("Ошибка поиска всех стран ", e);
        }
        return allCountry;
    }

    @Override
    public Map<Integer, String> getAllRegion(int countryId) {
        Map<Integer, String> allRegion = new HashMap<>();
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement prepare = connection.prepareStatement("SELECT id, name FROM " + REGION_TABLE
                     + " WHERE country_id=?")) {
            prepare.setInt(1, countryId);
            ResultSet resultSet = prepare.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String nameRegion = resultSet.getString(2);
                allRegion.put(id, nameRegion);
            }
        } catch (Exception e) {
            LOG.error("Ошибка поиска всех регионов по id: " + countryId, e);
        }
        return allRegion;
    }

    @Override
    public Map<Integer, String> getAllCity(int regionId) {
        Map<Integer, String> allCities = new HashMap<>();
        try (Connection connection = baseCommonPool.getConnect();
             PreparedStatement prepare = connection.prepareStatement("SELECT city.id, city.name FROM city INNER JOIN"
                     + " region ON  city.region_id=region.id WHERE region.country_id=?")) {
            prepare.setInt(1, regionId);
            ResultSet resultSet = prepare.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String nameRegion = resultSet.getString(2);
                allCities.put(id, nameRegion);
            }
        } catch (Exception e) {
            LOG.error("Ошибка поиска всех городов по id: " + regionId, e);
        }
        return allCities;
    }
}
