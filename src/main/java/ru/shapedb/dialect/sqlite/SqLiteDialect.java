package ru.shapedb.dialect.sqlite;

import ru.shapedb.database.conditions.*;
import ru.shapedb.database.dialect.SqlTranslatable;
import ru.shapedb.database.dialect.SqlDialect;
import ru.shapedb.database.statement.base.SqlStatementBase;
import ru.shapedb.database.statement.base.column.SqlCreateColumn;
import ru.shapedb.database.statement.base.column.SqlExistsColumn;
import ru.shapedb.database.statement.base.data.*;
import ru.shapedb.database.statement.base.data.SqlGetData;
import ru.shapedb.database.statement.base.table.SqlCreateTable;
import ru.shapedb.database.statement.base.table.SqlExistsTable;
import ru.shapedb.database.types.DataType;
import ru.shapedb.database.types.DataTypeParam;
import ru.shapedb.database.types.ForeignKeyRelation;
import ru.shapedb.database.types.SqlType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SqLiteDialect implements SqlDialect {

    @Override
    public String extract(SqlTranslatable object) {
        if (object instanceof DataType type) return dataTypeFormat(type);
        else if (object instanceof SqlStatementBase operation) return operationFormat(operation);
        return "";
    }

    private String quoteIdentifier(String name) {
        return "`" + name.replace("`", "``") + "`";
    }

    private String dataTypeFormat(DataType dataType) {
        SqlType type = dataType.getType();
        String predicate = "%s%s %s";

        String sqlType = switch (type) {
            case CHAR, VARCHAR, NCHAR, NVARCHAR, CLOB -> "TEXT";
            case TIME, DATE, TIMESTAMP -> "TEXT";
            case INTERVAL -> "INTEGER";
            case BINARY, VARBINARY, BLOB -> "BLOB";
            case DECIMAL -> "TEXT";
            case INTEGER, SMALLINT, BIGINT -> "INTEGER";
            case BOOLEAN -> "INTEGER";
            case FLOAT, DOUBLE, REAL -> "REAL";
        };

        String paramsPart;
        List<Integer> parameters = dataType.getParameters();
        if (parameters.isEmpty()) {
            paramsPart = "";
        } else if (type == SqlType.CHAR || type == SqlType.VARCHAR ||
                type == SqlType.NCHAR || type == SqlType.NVARCHAR) {
            paramsPart = "(%d)".formatted(parameters.get(0));
        } else {
            paramsPart = "";
        }

        StringBuilder constraintsPart = new StringBuilder();
        Map<DataTypeParam, String> constraints = dataType.getConstraints();
        for (Map.Entry<DataTypeParam, String> entry : constraints.entrySet()) {
            DataTypeParam param = entry.getKey();
            String value = entry.getValue();

            String paramSql = switch (param) {
                case CONSTRAINT -> "CONSTRAINT %s ".formatted(value);
                case DEFAULT -> "DEFAULT %s ".formatted(value); //fix
                case NOT_NULL -> "NOT NULL ";
                case CHECK -> "CHECK (%s) ".formatted(value); //fix
            };
            constraintsPart.append(paramSql);
        }

        ForeignKeyRelation fk = dataType.getFkRelation();
        if(fk != null){
            constraintsPart.append("REFERENCES %s(%s) ".formatted(fk.otherTable(), fk.otherColumn()));
        }

        return predicate.formatted(sqlType, paramsPart, constraintsPart);
    }

    private String operationFormat(SqlStatementBase operation) {
        return switch (operation) {
            case SqlCreateTable sqlCreateTable -> {
                String exist = sqlCreateTable.isIfNotExists() ? "IF NOT EXISTS " : "";
                String tableName = quoteIdentifier(sqlCreateTable.getTableName());
                List<String> schemaParts = sqlCreateTable.getConditions().stream()
                        .map(this::conditionFormat)
                        .collect(Collectors.toList());
                String columnsPart = String.join(", ", schemaParts);
                yield "CREATE TABLE %s%s (%s);".formatted(exist, tableName, columnsPart);
            }
            case SqlExistsTable sqlExistsTable ->
                    "SELECT EXISTS (SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = %s);"
                            .formatted(quoteStringLiteral(sqlExistsTable.getName()));
            case SqlUpdateData sqlUpdateData -> {
                String table = quoteIdentifier(sqlUpdateData.getTableName());
                List<String> setParts = sqlUpdateData.getAttributesConditions().stream()
                        .map(this::conditionFormat)
                        .collect(Collectors.toList());
                String setPart = String.join(", ", setParts);
                String wherePart = buildWhereClause(sqlUpdateData.getWhereConditions());
                yield "UPDATE %s SET %s%s;".formatted(table, setPart, wherePart);
            }
            case SqlInsertData sqlInsertData -> {
                String table = quoteIdentifier(sqlInsertData.getTableName());
                List<SqlInsertAttributeCondition> conditions = sqlInsertData.getAttributesConditions();
                List<String> columns = conditions.stream()
                        .map(SqlInsertAttributeCondition::getAttributeName)
                        .map(this::quoteIdentifier)
                        .toList();
                String columnsPart = columns.isEmpty() ? "" : columns.stream().collect(Collectors.joining(", ", " (", ")"));
                StringBuilder valuesPart = new StringBuilder();
                if (!conditions.isEmpty()) {
                    int rowCount = conditions.get(0).getAssociatedCount();
                    for (int i = 0; i < rowCount; i++) {
                        if (i > 0) valuesPart.append(", ");
                        valuesPart.append("(");
                        valuesPart.append("?".repeat(conditions.size()).chars()
                                .mapToObj(c -> "?")
                                .collect(Collectors.joining(", ")));
                        valuesPart.append(")");
                    }
                } else {
                    valuesPart.append("DEFAULT VALUES");
                }
                yield "INSERT INTO %s%s VALUES %s;".formatted(table, columnsPart, valuesPart);
            }
            case SqlGetData sqlGetData -> {
                String table = quoteIdentifier(sqlGetData.getTableName());
                String columns = sqlGetData.getColumnsSet() == null || sqlGetData.getColumnsSet().isEmpty()
                        ? "*" : String.join(", ", sqlGetData.getColumnsSet());
                String wherePart = buildWhereClause(sqlGetData.getWhereConditions());
                yield "SELECT %s FROM %s%s;".formatted(columns, table, wherePart);
            }
            case SqlExistsData sqlExistsData -> {
                String table = quoteIdentifier(sqlExistsData.getTableName());
                String wherePart = buildWhereClause(sqlExistsData.getWhereConditions());
                yield "SELECT EXISTS (SELECT 1 FROM %s%s);".formatted(table, wherePart);
            }
            case SqlCountData sqlCountData -> {
                String table = quoteIdentifier(sqlCountData.getTableName());
                String wherePart = buildWhereClause(sqlCountData.getWhereConditions());
                yield "SELECT COUNT(*) FROM %s%s;".formatted(table, wherePart);
            }
            case SqlExistsColumn sqlExistsColumn -> {
                String tableName = quoteStringLiteral(sqlExistsColumn.getTableName());
                String columnName = quoteStringLiteral(sqlExistsColumn.getColumnName());
                yield "SELECT EXISTS (SELECT 1 FROM pragma_table_info(%s) WHERE name = %s);".formatted(tableName, columnName);
            }
            case SqlCreateColumn sqlCreateColumn -> {
                String table = quoteIdentifier(sqlCreateColumn.getTableName());
                String column = quoteIdentifier(sqlCreateColumn.getColumnName());
                String type = dataTypeFormat(sqlCreateColumn.getType());

                boolean notNull = sqlCreateColumn.getType().getConstraints().containsKey(DataTypeParam.NOT_NULL);
                boolean hasDefault = sqlCreateColumn.getType().getConstraints().containsKey(DataTypeParam.DEFAULT);
                yield "ALTER TABLE %s ADD COLUMN %s %s %s;".formatted(table, column, type,
                        (notNull && !hasDefault) ? "DEFAULT " + sqlCreateColumn.getType().getType().getDefaultValue() : "");
            }
            case null -> throw new UnsupportedOperationException("Operation is null");
            default -> throw new UnsupportedOperationException("Unsupported operation: " + operation.getClass().getSimpleName());
        };
    }

    private String quoteStringLiteral(String str) {
        return "'" + str.replace("'", "''") + "'";
    }

    private String buildWhereClause(List<SqlWhereCondition> conditions) {
        if (conditions.isEmpty()) return "";
        List<String> parts = conditions.stream()
                .map(this::conditionFormat)
                .collect(Collectors.toList());
        return " WHERE " + String.join(" AND ", parts);
    }

    private String conditionFormat(SqlCondition condition) {
        return switch (condition) {
            case SqlAttributeCondition sac ->
                    "%s = ?".formatted(quoteIdentifier(sac.getAttributeName()));
            case SqlSchemaCondition ssc -> {
                String name = quoteIdentifier(ssc.getName());
                String typePart = dataTypeFormat(ssc.getType());
                StringBuilder result = new StringBuilder();
                result.append(name).append(" ").append(typePart);
                if (ssc.isPrimaryKey()) {
                    result.append(" PRIMARY KEY");
                    if (ssc.isAutoIncrement()) {
                        result.append(" AUTOINCREMENT");
                    }
                }
                yield result.toString();
            }
            case SqlWhereCondition swc -> {
                String op = swc.getPrevOperator() != null ? swc.getPrevOperator() + " " : "";
                yield op + quoteIdentifier(swc.getColumn()) + " = ?";
            }
            case null -> throw new IllegalArgumentException("Condition is null");
            default -> throw new IllegalArgumentException("Unsupported condition: " + condition.getClass().getSimpleName());
        };
    }


    //TEST

    public static void main(String[] args){
        SqLiteDialect translator = new SqLiteDialect();

        List<SqlStatementBase> operations = List.of(
                new SqlCreateTable("table", true, false,
                        new SqlSchemaCondition("column1",
                                new DataType(SqlType.VARCHAR, 32), true, true),
                        new SqlSchemaCondition("column2",
                                new DataType(SqlType.INTEGER)
                                        .constraint(DataTypeParam.DEFAULT, "0")
                                        .constraint(DataTypeParam.NOT_NULL, "")
                                        .foreignKey("foreignTABLE", "foreignCOLUMN")
                                , false, true),
                        new SqlSchemaCondition("column3",
                                new DataType(SqlType.CHAR)
                                        .constraint(DataTypeParam.NOT_NULL, "")
                                , false, true),
                        new SqlSchemaCondition("column4",
                                new DataType(SqlType.BLOB)
                                        .constraint(DataTypeParam.NOT_NULL, "")
                                , false, true)
                ),
                new SqlExistsTable("test"),
                new SqlCreateColumn("test", "columnAdded",
                        new DataType(SqlType.VARCHAR, 32).constraint(DataTypeParam.NOT_NULL, "")),
                new SqlExistsColumn("test", "columnAdded"),
                new SqlInsertData("test",
                        new SqlInsertAttributeCondition("column1", "someMegaText"),
                        new SqlInsertAttributeCondition("column2", 0),
                        new SqlInsertAttributeCondition("column3", 'a'),
                        new SqlInsertAttributeCondition("column4", (Object) new byte[]{(byte) 0x123f}),
                        new SqlInsertAttributeCondition("columnAdded", "addedTextYYO")
                ),
                new SqlExistsData("test",
                        new SqlWhereCondition(null, "column2", 0)
                ),
                new SqlCountData("test",
                        new SqlWhereCondition(null, "column2", 0)
                ),
                new SqlUpdateData("test",
                        new SqlWhereCondition(null, "column2", 0)
                ).setDataToChange(new SqlAttributeCondition("column2", 1)),
                new SqlGetData("test", new SqlWhereCondition(null, "column2", 1))
                        .column("column1", String.class).column("column3", String.class)
        );



        for(SqlStatementBase operation : operations){
            String sql = translator.extract(operation);
            System.out.println(sql);
        }
    }
}