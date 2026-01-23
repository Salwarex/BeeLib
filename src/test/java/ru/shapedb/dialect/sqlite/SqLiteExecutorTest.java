package ru.shapedb.dialect.sqlite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shapedb.database.conditions.SqlAttributeCondition;
import ru.shapedb.database.conditions.SqlInsertAttributeCondition;
import ru.shapedb.database.conditions.SqlSchemaCondition;
import ru.shapedb.database.conditions.SqlWhereCondition;
import ru.shapedb.database.statement.Statement;
import ru.shapedb.database.repository.SqlRepository;
import ru.shapedb.database.statement.base.column.SqlCreateColumn;
import ru.shapedb.database.statement.base.data.SqlGetData;
import ru.shapedb.database.statement.base.data.SqlInsertData;
import ru.shapedb.database.statement.base.data.SqlUpdateData;
import ru.shapedb.database.statement.base.table.SqlCreateTable;
import ru.shapedb.database.types.DataType;
import ru.shapedb.database.types.SqlType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SqLiteExecutorTest {
    private static final String DB_URL = "jdbc:sqlite:test_db.db";
    private SqlRepository executor;
    private Connection conn;

    @BeforeEach
    void setUp() throws SQLException{
        conn = DriverManager.getConnection(DB_URL);
        executor = new SqlRepository(new SqLiteDialect(), DB_URL, null, null);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (conn != null) conn.close();
    }

    @Test
    void testCreateTableSimple() throws SQLException{
        SqlSchemaCondition[] conditions = new SqlSchemaCondition[]{
                new SqlSchemaCondition(
                        "id",
                        new DataType(SqlType.INTEGER),
                        true,
                        true
                ),
                new SqlSchemaCondition(
                        "phone",
                        new DataType(SqlType.VARCHAR, 15),
                        false,
                        false
                ),
                new SqlSchemaCondition(
                        "name",
                        new DataType(SqlType.VARCHAR, 30),
                        false,
                        false
                ),
        };

        Statement statement = new Statement(new SqlCreateTable("employee", true, false, conditions));
        executor.execute(statement);


        ResultSet rs = conn.createStatement()
                .executeQuery("SELECT EXISTS (SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = 'employee');");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        assertFalse(rs.next());
    }

    @Test
    void testCreateTableRelation() throws SQLException{
        SqlSchemaCondition[] addressConditions = new SqlSchemaCondition[]{
                new SqlSchemaCondition(
                        "id",
                        new DataType(SqlType.INTEGER),
                        true,
                        true
                ),
                new SqlSchemaCondition(
                        "address",
                        new DataType(SqlType.VARCHAR, 15),
                        false,
                        false
                ),
                new SqlSchemaCondition(
                        "employeeId",
                        new DataType(SqlType.VARCHAR, 30),
                        false,
                        false
                ),
        };

        SqlCreateTable base = new SqlCreateTable("address", true, false, addressConditions);
        Statement statement = new Statement(base);
        executor.execute(statement);

        ResultSet rs = conn.createStatement()
                .executeQuery("SELECT EXISTS (SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = 'address');");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        assertFalse(rs.next());
    }

    @Test
    void insertDataSimple() throws SQLException{
        SqlInsertAttributeCondition[] conditions = new SqlInsertAttributeCondition[]{
                new SqlInsertAttributeCondition(
                        "phone", "123591235812", "1384518", "2385389451"
                ),
                new SqlInsertAttributeCondition(
                        "name", "John", "Eval", "Vitaliy"
                ),
        };

        SqlInsertData base = new SqlInsertData("employee", conditions);

        Statement statement = new Statement(base);
        executor.execute(statement);

        ResultSet rs = conn.createStatement().executeQuery("SELECT phone FROM employee WHERE name = 'John'");
        assertTrue(rs.next());
        assertEquals("123591235812", rs.getString(1));
    }

    @Test
    void insertDataWithFK() throws SQLException{
        SqlInsertAttributeCondition[] conditions = new SqlInsertAttributeCondition[]{
                new SqlInsertAttributeCondition(
                        "address", "st. wotihjsieot", "correctStreet", "sruhs"
                ),
                new SqlInsertAttributeCondition(
                        "employeeId", 1, 2, 1
                ),
        };

        SqlInsertData base = new SqlInsertData("address", conditions);

        Statement statement = new Statement(base);
        executor.execute(statement);

        ResultSet rs = conn.createStatement().executeQuery("SELECT address FROM address WHERE employeeId = 2");
        assertTrue(rs.next());
        assertEquals("correctStreet", rs.getString(1));
    }

    @Test
    void getData() throws SQLException{
        SqlGetData base = new SqlGetData("employee", new SqlWhereCondition(null, "name", "John"));
        base.column("phone", String.class);

        Statement statement = new Statement(base);
        executor.execute(statement);

        String result = base.getCastedResult("phone");

        assertEquals("123591235812", result);
    }

    @Test
    void alterTable() throws SQLException{
        SqlCreateColumn base = new SqlCreateColumn("employee", "newColumn",
                new DataType(SqlType.DATE));

        Statement statement = new Statement(base);
        executor.execute(statement);

        ResultSet rs = conn.createStatement()
                .executeQuery("SELECT EXISTS (SELECT 1 FROM pragma_table_info('employee') WHERE name = 'newColumn');");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        assertFalse(rs.next());
    }

    @Test
    void updateData() throws SQLException{
        SqlUpdateData base = new SqlUpdateData("employee",
                new SqlWhereCondition(null, "name", "John"));
        base.setDataToChange(new SqlAttributeCondition("name", "Johan"));

        Statement statement = new Statement(base);
        executor.execute(statement);

        ResultSet rs = conn.createStatement().executeQuery("SELECT EXISTS (SELECT 1 FROM employee WHERE name = 'Johan');");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        assertFalse(rs.next());
    }
}
