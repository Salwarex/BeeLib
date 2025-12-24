package ru.shapeorm.dialect.mysql.objects;

import ru.shapeorm.database.query.SqlQueryResult;
import ru.shapeorm.database.query.argument.alter.AlterArgument;
import ru.shapeorm.database.query.argument.alter.AlterDataArgument;
import ru.shapeorm.database.query.argument.count.CountArgument;
import ru.shapeorm.database.query.argument.count.CountDataArgument;
import ru.shapeorm.database.query.argument.create.CreateArgument;
import ru.shapeorm.database.query.argument.create.CreateDataArgument;
import ru.shapeorm.database.query.argument.delete.DeleteArgument;
import ru.shapeorm.database.query.argument.delete.DeleteDataArgument;
import ru.shapeorm.database.query.argument.get.GetArgument;
import ru.shapeorm.database.query.argument.get.GetDataArgument;
import ru.shapeorm.database.query.objects.SqlData;

public class MySqlData extends SqlData {
    @Override
    public void create(CreateArgument argument) {
        super.create(argument);
        CreateDataArgument arg = (CreateDataArgument) argument;
    }

    @Override
    public void delete(DeleteArgument argument) {
        super.delete(argument);
        DeleteDataArgument arg = (DeleteDataArgument) argument;
    }

    @Override
    public void alter(AlterArgument argument) {
        super.alter(argument);
        AlterDataArgument arg = (AlterDataArgument) argument;
    }

    @Override
    public SqlQueryResult<?> get(GetArgument argument) {
        super.get(argument);
        GetDataArgument arg = (GetDataArgument) argument;
        return null;
    }

    @Override
    public int count(CountArgument argument) {
        super.count(argument);
        CountDataArgument arg = (CountDataArgument) argument;
        return 0;
    }
}
