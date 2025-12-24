package ru.shapeorm.database.query.objects;

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

public abstract class SqlTable implements SqlObject{
    @Override
    public void create(CreateArgument argument) {
        Class<?> clazz = CreateTableArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public void delete(DeleteArgument argument) {
        Class<?> clazz = DeleteTableArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public void alter(AlterArgument argument) {
        Class<?> clazz = AlterTableArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
    }

    @Override
    public SqlQueryResult<?> get(GetArgument argument) {
        Class<?> clazz = GetTableArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
        return null;
    }

    @Override
    public int count(CountArgument argument) {
        Class<?> clazz = CountTableArgument.class;
        if(argument.getClass().isAssignableFrom(clazz))
            throw new IllegalArgumentException("Incorrect Argument: %s, Needed: %s"
                    .formatted(argument.getClass().getName(), clazz.getName()));
        return 0;
    }
}
