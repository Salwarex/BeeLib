package ru.waxera.beeLib.utils.data.database;

import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.data.database.query.QueryWherePair;
import ru.waxera.beeLib.utils.message.Message;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A class for interacting with SQL databases through built-in methods.
 *
 * <p>
 * This class describes only methods for directly simplifying interaction
 * with the database, bypassing writing SQL queries. Some of the methods
 * in this class may seem unoptimized or thoughtless, but the main purpose
 * of their creation is to simplify interaction. There may be changes in the
 * future to achieve greater optimization. Since version 2 uses {@link QueryWherePair}.
 * </p>
 *
 * @version 2 (v1.4)
 * @since v1.0
 * @author Salwarex
 */

public class Database {
    private final String url;
    private final String user;
    private final String password;
    private final DatabaseType type;

    public Database(DatabaseType db_type, String connectionString, String user, String password){
        this.type = db_type;
        this.user = user;
        this.password = password;
        switch (this.type){
            case MYSQL -> {
                this.url = "jdbc:mysql://" + connectionString;
                break;
            }
            case SQLITE -> {
                this.url = "jdbc:sqlite:" + connectionString;
                break;
            }
            default -> {
                this.url = null;
                Message.error(null, "The specified database type could not be found!");
                return;
            }
        }
    }

    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url, user, password);
    }

    public void createTable(String tableName, String data_info){
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" + data_info + ");");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public boolean existsTable(String tableName, Connection connection) throws SQLException {
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, tableName, null);
        return rs.next();
    }

    public void createColumn(String tableName, String column) {
        try (Connection connection = getConnection()) {
            if (existsTable(tableName, connection) && !existsColumn(tableName, column, connection)) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("ALTER TABLE " + tableName + " ADD " + column + ";");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public boolean existsColumn(String table, String columnName, Connection connection) throws SQLException {
        if (existsTable(table, connection)) {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getColumns(null, null, table, columnName);
            return rs.next();
        }
        return false;
    }

    public void insert(String table_name, String attributes, Object... values) {
        if (attributes.split(", ").length == values.length) {
            StringBuilder valuesPaster = new StringBuilder();
            for (int i = 0; i < attributes.split(", ").length; i++) { valuesPaster.append("?, "); }
            valuesPaster.setLength(valuesPaster.length() - 2);

            String sql = "INSERT INTO " + table_name + " (" + attributes + ") VALUES (" + valuesPaster + ");";

            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int i = 1;
                for (Object obj : values) {
                    preparedStatement.setObject(i, obj);
                    i++;
                }
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    public void insert(String table_name, Object... values) {
        StringBuilder valuesPaster = new StringBuilder();
        for (int i = 0; i < values.length; i++) { valuesPaster.append("?, "); }
        valuesPaster.setLength(valuesPaster.length() - 2);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + table_name + " VALUES (" + valuesPaster + ");")) {
            int i = 1;
            for (Object obj : values) {
                preparedStatement.setObject(i, obj);
                i++;
            }
            System.out.println("execute insert...");
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * WHERE-HANDLERS
     * The where_info hashmap is used as a container for information used in SQL queries in the format: ... WHERE <KEY> = <VALUE>
     * The where_ands array is used to transmit the necessary condition values. Might be null.
     */

    public boolean existsData(String table_name,
                              QueryWherePair ... queryWherePairs){
        //ArrayList<Object> objects = prepareData(new ArrayList<>(where_info.values()));
        StringBuilder sql = new StringBuilder("SELECT * FROM " + table_name + " WHERE ");
        int i = 0;
        for(QueryWherePair pair : queryWherePairs){
            sql.append(pair.toString()).append((i == queryWherePairs.length - 1) ? ";" : "");
            i++;
        }
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())){
            int index = 1;
            for(QueryWherePair pair : queryWherePairs){
                Object value = pair.getValue();
                preparedStatement.setObject(index, value);
                index++;
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void updateData(String table_name,
                           String update_column,
                           Object new_data,
                           QueryWherePair ... queryWherePairs){
        new_data = prepareData(new_data);
        //ArrayList<Object> objects;
        StringBuilder sql = new StringBuilder("UPDATE " + table_name + " SET " + update_column + " = ?" + (queryWherePairs.length > 0 ? " WHERE " : ""));
        int i = 0;
        for(QueryWherePair pair : queryWherePairs){
            sql.append(pair.toString()).append((i == queryWherePairs.length - 1) ? ";" : "");
            i++;
        }
        //else objects = new ArrayList<>();

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())){
            preparedStatement.setObject(1, new_data);
            int index = 2;
            for (QueryWherePair pair : queryWherePairs) {
                Object value = pair.getValue();
                preparedStatement.setObject(index, value);
                index++;
            }
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Object getData(String column_name,
                          String table_name,
                          QueryWherePair ... queryWherePairs){
        //ArrayList<Object> objects = prepareData(new ArrayList<>(where_info.values()));
        StringBuilder sql = new StringBuilder("SELECT " + column_name + " FROM " + table_name + (queryWherePairs.length > 0 ? " WHERE " : ""));
        int i = 0;
        for (QueryWherePair pair : queryWherePairs) {
            sql.append(pair.toString()).append((i == queryWherePairs.length - 1) ? ";" : "");
            i++;
        }
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())){
            int index = 1;
            for (QueryWherePair pair : queryWherePairs) {
                Object value = pair.getValue();
                preparedStatement.setObject(index, value);
                index++;
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getObject(column_name);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ArrayList<ArrayList<Object>> getDataObjects(String select_modifier,
                                                       String[] providedColumns,
                                                       String table_name, QueryWherePair ... queryWherePairs){
        ArrayList<ArrayList<Object>> result = new ArrayList<>();

        //ArrayList<Object> objects = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT " + select_modifier + " FROM " + table_name + (queryWherePairs.length > 0 ? " WHERE " : ""));
        int i = 0;
        for(QueryWherePair pair : queryWherePairs){
            if(pair != null){
                sql.append(pair.toString()).append((i == queryWherePairs.length - 1) ? ";" : "");
                i++;
            }
        }

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())){
            int index = 1;
            for (QueryWherePair pair : queryWherePairs) {
                if (pair != null) {
                    Object value = pair.getValue();
                    preparedStatement.setObject(index, value);
                    index++;
                }
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ArrayList<Object> row = new ArrayList<>();
                for (String column : providedColumns) {
                    row.add(resultSet.getObject(column));
                }
                result.add(row);
            }
            return result;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int count(String table_name, QueryWherePair ... queryWherePairs){
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM " + table_name + (queryWherePairs.length > 0 ? " WHERE " : ""));
        //ArrayList<Object> objects = new ArrayList<>();
        int i = 0;
        for(QueryWherePair pair : queryWherePairs){
            sql.append(pair.toString()).append((i == queryWherePairs.length - 1) ? ";" : "");
            i++;
        }
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())){
            int index = 1;
            for (QueryWherePair pair : queryWherePairs) {
                Object value = pair.getValue();
                preparedStatement.setObject(index, value);
                index++;
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public void execSQL(String sql) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Deprecated
    private Object prepareData(Object check){
        if (check != null && check.toString().equalsIgnoreCase("CURRENT_DATE")) {
            return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        }
        return check;
    }

    @Deprecated
    private ArrayList<Object> prepareData(ArrayList<Object> objects) {
        ArrayList<Object> result = new ArrayList<>();
        for(Object check : objects){
            result.add(prepareData(check));
        }
        return result;
    }
}
