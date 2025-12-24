package ru.shapeorm.database.query.objects;

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

public abstract class SqlColumn implements SqlObject{
    @Override
    public void create(CreateArgument argument) {
        Class<?> clazz = CreateColumnArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public void delete(DeleteArgument argument) {
        Class<?> clazz = DeleteColumnArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public void alter(AlterArgument argument) {
        Class<?> clazz = AlterColumnArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public SqlQueryResult<?> get(GetArgument argument) {
        Class<?> clazz = GetColumnArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
        return null;
    }

    @Override
    public int count(CountArgument argument) {
        Class<?> clazz = CountColumnArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
        return 0;
    }
}
