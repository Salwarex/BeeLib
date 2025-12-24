package ru.shapeorm.database.query.objects;

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

public abstract class SqlData implements SqlObject{
    @Override
    public void create(CreateArgument argument) {
        Class<?> clazz = CreateDataArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public void delete(DeleteArgument argument) {
        Class<?> clazz = DeleteDataArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public void alter(AlterArgument argument) {
        Class<?> clazz = AlterDataArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public SqlQueryResult<?> get(GetArgument argument) {
        Class<?> clazz = GetDataArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
        return null;
    }

    @Override
    public int count(CountArgument argument) {
        Class<?> clazz = CountDataArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
        return 0;
    }
}
