package ru.shapeorm.database.query.objects;

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

public abstract class SqlIndex implements SqlObject{
    @Override
    public void create(CreateArgument argument) {
        Class<?> clazz = CreateIndexArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public void delete(DeleteArgument argument) {
        Class<?> clazz = DeleteIndexArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public void alter(AlterArgument argument) {
        Class<?> clazz = AlterIndexArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public SqlQueryResult<?> get(GetArgument argument) {
        Class<?> clazz = GetIndexArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
        return null;
    }

    @Override
    public int count(CountArgument argument) {
        Class<?> clazz = CountIndexArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
        return 0;
    }
}
