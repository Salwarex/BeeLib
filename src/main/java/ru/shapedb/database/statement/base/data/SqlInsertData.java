package ru.shapedb.database.statement.base.data;

import ru.shapedb.database.conditions.PreparedCondition;
import ru.shapedb.database.conditions.SqlInsertAttributeCondition;
import ru.shapedb.database.statement.base.SqlOperationUpdate;
import ru.shapedb.database.statement.base.SqlPreparedOperation;
import ru.shapedb.database.statement.base.SqlStatementBase;

import java.util.ArrayList;
import java.util.List;

public class SqlInsertData extends SqlStatementBase implements SqlPreparedOperation, SqlOperationUpdate {

    private final String tableName;
    private final List<SqlInsertAttributeCondition> attributesConditions;

    public SqlInsertData(String tableName, SqlInsertAttributeCondition ... attributesConditions){
        this.tableName = tableName;

        int foundedAssociatedCount = -1;

        this.attributesConditions = new ArrayList<>();

        for(SqlInsertAttributeCondition attributeCondition : attributesConditions){
            if(attributeCondition.getAssociatedCount() != foundedAssociatedCount
                    && foundedAssociatedCount != -1) throw new IllegalArgumentException("Submitted attribute conditions not associated!");
            else if (foundedAssociatedCount == -1) foundedAssociatedCount = attributeCondition .getAssociatedCount();
            this.attributesConditions.add(attributeCondition);
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<SqlInsertAttributeCondition> getAttributesConditions() {
        return attributesConditions;
    }

    @Override
    public List<PreparedCondition> getPreparedConditions() {
        return new ArrayList<>(attributesConditions);
    }
}
