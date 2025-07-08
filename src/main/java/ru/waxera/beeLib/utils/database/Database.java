package ru.waxera.beeLib.utils.database;

import ru.waxera.beeLib.utils.message.Message;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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

            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + table_name + " (" + attributes + ") VALUES (" + valuesPaster + ");")) {
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
                              HashMap<String, Object> where_info,
                              boolean[] where_ands){
        ArrayList<Object> objects = prepareData(new ArrayList<>(where_info.values()));
        StringBuilder sql = new StringBuilder("SELECT * FROM " + table_name + " WHERE ");
        int i = 0;
        for(String key : where_info.keySet()){
            String suffix = (i != where_info.size() - 1) ? (where_ands != null ? (where_ands[i] ? " AND " : " OR ") : " AND ") : ";";
            sql.append(key).append(" =  ?").append(suffix);
            i++;
        }
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())){
            int index = 1;
            for(Object value : objects){
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
                           HashMap<String, Object> where_info,
                           boolean[] where_ands){
        new_data = prepareData(new_data);
        ArrayList<Object> objects = prepareData(new ArrayList<>(where_info.values()));
        StringBuilder sql = new StringBuilder("UPDATE " + table_name + " SET " + update_column + " = ? WHERE ");
        int i = 0;
        for(String key : where_info.keySet()){
            String suffix = (i != where_info.size() - 1) ? (where_ands != null ? (where_ands[i] ? " AND " : " OR ") : " AND ") : ";";
            sql.append(key).append(" =  ?").append(suffix);
            i++;
        }
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())){
            preparedStatement.setObject(1, new_data);
            int index = 2;
            for(Object value : objects){
                preparedStatement.setObject(index, value);
                index++;
            }
            ResultSet resultSet = preparedStatement.executeQuery();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Object getData(String column_name,
                          String table_name,
                          HashMap<String, Object> where_info,
                          boolean[] where_ands){
        ArrayList<Object> objects = prepareData(new ArrayList<>(where_info.values()));
        StringBuilder sql = new StringBuilder("SELECT " + column_name + " FROM " + table_name + " WHERE ");
        int i = 0;
        for(String key : where_info.keySet()){
            String suffix = (i != where_info.size() - 1) ? (where_ands != null ? (where_ands[i] ? " AND " : " OR ") : " AND ") : ";";
            sql.append(key).append(" =  ?").append(suffix);
            i++;
        }
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())){
            int index = 1;
            for(Object value : objects){
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
        return false;
    }

    public ArrayList<ArrayList<Object>> getDataObjects(String select_modifier,
                                                       String[] providedColumns,
                                                       String table_name,
                                                       HashMap<String, Object> where_info,
                                                       boolean[] where_ands){
        ArrayList<ArrayList<Object>> result = new ArrayList<>();

        ArrayList<Object> objects = prepareData(new ArrayList<>(where_info.values()));
        StringBuilder sql = new StringBuilder("SELECT " + select_modifier + " FROM " + table_name + " WHERE ");
        int i = 0;
        for(String key : where_info.keySet()){
            String suffix = (i != where_info.size() - 1) ? (where_ands != null ? (where_ands[i] ? " AND " : " OR ") : " AND ") : ";";
            sql.append(key).append(" =  ?").append(suffix);
            i++;
        }
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())){
            int index = 1;
            for(Object value : where_info.values()){
                preparedStatement.setObject(index, value);
                index++;
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ArrayList<Object> row = new ArrayList<>();
                for (String column : providedColumns) {
                    row.add(resultSet.getObject(column));
                }
                result.add(row);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void execSQL(String sql) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Object prepareData(Object check){
        if (check != null && check.toString().equalsIgnoreCase("CURRENT_DATE")) {
            return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        }
        return check;
    }
    private ArrayList<Object> prepareData(ArrayList<Object> objects) {
        ArrayList<Object> result = new ArrayList<>();
        for(Object check : objects){
            result.add(prepareData(check));
        }
        return result;
    }
}
