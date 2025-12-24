package ru.waxera.beeLib.utils.data.database.types.dialects;

import ru.waxera.beeLib.utils.data.database.types.DataType;

public interface DatabaseDialect {
    String convert(DataType dataType);
}
