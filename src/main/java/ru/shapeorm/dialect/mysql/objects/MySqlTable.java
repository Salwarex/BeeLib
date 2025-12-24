package ru.shapeorm.dialect.mysql.objects;

import ru.shapeorm.database.query.SqlQueryResult;
import ru.shapeorm.database.query.argument.alter.AlterArgument;
import ru.shapeorm.database.query.argument.alter.AlterTableArgument;
import ru.shapeorm.database.query.argument.count.CountArgument;
import ru.shapeorm.database.query.argument.count.CountTableArgument;
import ru.shapeorm.database.query.argument.create.CreateArgument;
import ru.shapeorm.database.query.argument.create.CreateTableArgument;
import ru.shapeorm.database.query.argument.delete.DeleteArgument;
import ru.shapeorm.database.query.argument.delete.DeleteTableArgument;
import ru.shapeorm.database.query.argument.get.GetArgument;
import ru.shapeorm.database.query.argument.get.GetTableArgument;
import ru.shapeorm.database.query.objects.SqlTable;

public class MySqlTable extends SqlTable {
    @Override
    public void create(CreateArgument argument) {
        super.create(argument);
        CreateTableArgument arg = (CreateTableArgument) argument;
    }

    @Override
    public void delete(DeleteArgument argument) {
        super.delete(argument);
        DeleteTableArgument arg = (DeleteTableArgument) argument;
    }

    @Override
    public void alter(AlterArgument argument) {
        super.alter(argument);
        AlterTableArgument arg = (AlterTableArgument) argument;
    }

    @Override
    public SqlQueryResult<?> get(GetArgument argument) {
        super.get(argument);
        GetTableArgument arg = (GetTableArgument) argument;
        return null;
    }

    @Override
    public int count(CountArgument argument) {
        super.count(argument);
        CountTableArgument arg = (CountTableArgument) argument;
        return 0;
    }
}
