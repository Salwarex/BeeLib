package ru.shapeorm.dialect.mysql.objects;

import ru.shapeorm.database.query.SqlQueryResult;
import ru.shapeorm.database.query.argument.alter.AlterArgument;
import ru.shapeorm.database.query.argument.alter.AlterIndexArgument;
import ru.shapeorm.database.query.argument.count.CountArgument;
import ru.shapeorm.database.query.argument.count.CountIndexArgument;
import ru.shapeorm.database.query.argument.create.CreateArgument;
import ru.shapeorm.database.query.argument.create.CreateIndexArgument;
import ru.shapeorm.database.query.argument.delete.DeleteArgument;
import ru.shapeorm.database.query.argument.delete.DeleteIndexArgument;
import ru.shapeorm.database.query.argument.get.GetArgument;
import ru.shapeorm.database.query.argument.get.GetIndexArgument;
import ru.shapeorm.database.query.objects.SqlIndex;

public class MySqlIndex extends SqlIndex {
    @Override
    public void create(CreateArgument argument) {
        super.create(argument);
        CreateIndexArgument arg = (CreateIndexArgument) argument;
    }

    @Override
    public void delete(DeleteArgument argument) {
        super.delete(argument);
        DeleteIndexArgument arg = (DeleteIndexArgument) argument;
    }

    @Override
    public void alter(AlterArgument argument) {
        super.alter(argument);
        AlterIndexArgument arg = (AlterIndexArgument) argument;
    }

    @Override
    public SqlQueryResult<?> get(GetArgument argument) {
        super.get(argument);
        GetIndexArgument arg = (GetIndexArgument) argument;
        return null;
    }

    @Override
    public int count(CountArgument argument) {
        super.count(argument);
        CountIndexArgument arg = (CountIndexArgument) argument;
        return 0;
    }
}
