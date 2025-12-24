package ru.shapeorm.database.query.objects;

import ru.shapeorm.database.query.SqlQueryResult;
import ru.shapeorm.database.query.argument.alter.AlterArgument;
import ru.shapeorm.database.query.argument.count.CountArgument;
import ru.shapeorm.database.query.argument.create.CreateArgument;
import ru.shapeorm.database.query.argument.delete.DeleteArgument;
import ru.shapeorm.database.query.argument.get.GetArgument;

public interface SqlObject {
    void create(CreateArgument argument);
    void delete(DeleteArgument argument);
    void alter(AlterArgument argument);
    SqlQueryResult<?> get(GetArgument argument);
    int count(CountArgument argument);
}
