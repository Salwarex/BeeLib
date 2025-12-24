package ru.shapeorm.dialect.sqlite.objects;

import ru.shapeorm.database.query.SqlQueryResult;
import ru.shapeorm.database.query.argument.alter.AlterArgument;
import ru.shapeorm.database.query.argument.alter.AlterColumnArgument;
import ru.shapeorm.database.query.argument.count.CountArgument;
import ru.shapeorm.database.query.argument.count.CountColumnArgument;
import ru.shapeorm.database.query.argument.create.CreateArgument;
import ru.shapeorm.database.query.argument.create.CreateColumnArgument;
import ru.shapeorm.database.query.argument.delete.DeleteArgument;
import ru.shapeorm.database.query.argument.delete.DeleteColumnArgument;
import ru.shapeorm.database.query.argument.get.GetArgument;
import ru.shapeorm.database.query.argument.get.GetColumnArgument;
import ru.shapeorm.database.query.objects.SqlColumn;

public class SqLiteColumn extends SqlColumn {
    @Override
    public void create(CreateArgument argument) {
        super.create(argument);
        CreateColumnArgument arg = (CreateColumnArgument) argument;
    }

    @Override
    public void delete(DeleteArgument argument) {
        super.delete(argument);
        DeleteColumnArgument arg = (DeleteColumnArgument) argument;
    }

    @Override
    public void alter(AlterArgument argument) {
        super.alter(argument);
        AlterColumnArgument arg = (AlterColumnArgument) argument;
    }

    @Override
    public SqlQueryResult<?> get(GetArgument argument) {
        super.get(argument);
        GetColumnArgument arg = (GetColumnArgument) argument;
        return null;
    }

    @Override
    public int count(CountArgument argument) {
        super.count(argument);
        CountColumnArgument arg = (CountColumnArgument) argument;
        return 0;
    }
}
