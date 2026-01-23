package ru.shapedb.database.repository;

import ru.shapedb.database.conditions.PreparedCondition;
import ru.shapedb.database.conditions.SqlInsertAttributeCondition;
import ru.shapedb.database.dialect.SqlDialect;
import ru.shapedb.database.statement.Statement;
import ru.shapedb.database.statement.base.*;

import java.sql.*;
import java.util.List;

/**
 * A class for interacting with SQL databases through built-in methods.
 *
 * <p>
 * This class allows you to use the ShapeDB module, which opens up the possibility of
 * lightweight use of Java code bypassing writing SQL queries. Starting with v1.4,
 * it replaced the old Database class.
 * </p>
 *
 * @version 1
 * @since v1.4
 * @author Vitaliy
 */

public class SqlRepository implements DatabaseRepository {
    private final SqlDialect extractor;
    private final String url;
    private final String user;
    private final String password;

    public SqlRepository(SqlDialect extractor, String url, String user, String password) {
        this.extractor = extractor;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void execute(Statement statement) {
        SqlStatementBase operation = statement.getBase();
        String sql = extractor.extract(operation);

        System.out.printf("Executing: %s%n", sql);

        try(
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            executeOperation(operation, connection, preparedStatement);
        }catch (SQLException e){
            System.err.printf("Execute SQL-query error: {%s}%n", e.toString());
        }
    }

    @Override
    public void executeTransaction(Statement... queries){
        if (queries == null || queries.length == 0) {
            return;
        }

        try(Connection connection = getConnection()){
            connection.setAutoCommit(false);
            try{
                for (Statement statement : queries) {
                    SqlStatementBase operation = statement.getBase();
                    String sql = extractor.extract(operation);

                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        executeOperation(operation, connection, preparedStatement);
                    }
                }

                connection.commit();
            } catch (SQLException e){
                try {
                    connection.rollback();

                } catch (SQLException rollbackEx) {
                    System.err.println("Failed to rollback transaction: " + rollbackEx.getMessage());
                    e.addSuppressed(rollbackEx);
                }
            }
        }catch (SQLException e){
            System.err.printf("Execute SQL-query error: {%s}%n", e.toString());
        }
    }

    private void executeOperation(SqlStatementBase operation, Connection connection, PreparedStatement preparedStatement) throws SQLException {
        if(operation instanceof SqlPreparedOperation pOperation){
            List<PreparedCondition> conditions = pOperation.getPreparedConditions();
            List<SqlInsertAttributeCondition> insertConditions = conditions.stream()
                    .filter(SqlInsertAttributeCondition.class::isInstance)
                    .map(SqlInsertAttributeCondition.class::cast)
                    .toList();

            List<PreparedCondition> simpleConditions = conditions.stream()
                    .filter(cond -> !(cond instanceof SqlInsertAttributeCondition))
                    .toList();

            int recordCount = insertConditions.isEmpty() ? 0 : insertConditions.getFirst().getAssociatedCount();

            int paramIndex = 1;

            for (int recordIdx = 0; recordIdx < recordCount; recordIdx++) {
                for (SqlInsertAttributeCondition insertCond : insertConditions) {
                    Object value = insertCond.getValue(recordIdx);
                    preparedStatement.setObject(paramIndex, value);
                    paramIndex++;
                }
            }

            for (PreparedCondition simpleCond : simpleConditions) {
                Object value = simpleCond.getValue();
                preparedStatement.setObject(paramIndex, value);
                paramIndex++;
            }
        }

        if(operation instanceof SqlOperationExecute){
            preparedStatement.execute();
        }
        else if(operation instanceof SqlOperationUpdate){
            preparedStatement.executeUpdate();
        } else if (operation instanceof SqlOperationQuery sqlOperationQuery) {
            ResultSet result = preparedStatement.executeQuery();
            sqlOperationQuery.provideResult(result);
        }
    }
}
