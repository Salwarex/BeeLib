package ru.waxera.beeLib.utils.data.database.object;

@FunctionalInterface
public interface DatabaseObjectUpdateFunction {
    void run(DatabaseObject<?> object);
}
