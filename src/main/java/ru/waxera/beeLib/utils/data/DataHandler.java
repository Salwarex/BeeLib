package ru.waxera.beeLib.utils.data;

import ru.waxera.beeLib.utils.data.database.Database;
import ru.waxera.beeLib.utils.data.database.DatabaseType;

/**
 * An abstract class that using for creation child-classes
 * for interaction between the user and the {@link Database database}.
 *
 * @see Database
 * @see DatabaseType
 * @author Salwarex
 * @version 1
 * @since v1.4
 */

public abstract class DataHandler {
    protected final Database database;
    public DataHandler(
            DatabaseType type,
            String connectionString,

            String user,
            String password
    ){
        this.database = new Database(type, connectionString, user, password);
    }
}
