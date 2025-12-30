package ru.shapedb.database.types;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public enum SqlType {
    CHAR("''") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getString(column);
        }
    },
    VARCHAR("''") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getString(column);
        }
    },
    NCHAR("''") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getNString(column);
        }
    },
    NVARCHAR("''") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getNString(column);
        }
    },
    BINARY("X''") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getBytes(column);
        }
    },
    VARBINARY("X''") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getBytes(column);
        }
    },
    DECIMAL("0") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getBigDecimal(column);
        }
    },
    INTEGER("0") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            int val = rs.getInt(column);
            return rs.wasNull() ? null : val;
        }
    },
    SMALLINT("0") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            short val = rs.getShort(column);
            return rs.wasNull() ? null : val;
        }
    },
    BIGINT("0") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            long val = rs.getLong(column);
            return rs.wasNull() ? null : val;
        }
    },
    FLOAT("0.0") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            float val = rs.getFloat(column);
            return rs.wasNull() ? null : val;
        }
    },
    DOUBLE("0.0") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            double val = rs.getDouble(column);
            return rs.wasNull() ? null : val;
        }
    },
    REAL("0.0") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            float val = rs.getFloat(column);
            return rs.wasNull() ? null : val;
        }
    },
    BOOLEAN("0") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            boolean val = rs.getBoolean(column);
            return rs.wasNull() ? null : val;
        }
    },
    DATE("'1970-01-01'") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getDate(column);
        }
    },
    TIME("'00:00:00'") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getTime(column);
        }
    },
    TIMESTAMP("CURRENT_TIMESTAMP") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getTimestamp(column);
        }
    },
    BLOB("X''") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getBlob(column);
        }
    },
    CLOB("''") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getClob(column);
        }
    },
    INTERVAL("0") {
        @Override
        public Object getValue(ResultSet rs, String column) throws SQLException {
            return rs.getString(column);
        }
    };

    private final String defaultValue;

    SqlType(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public abstract Object getValue(ResultSet rs, String column) throws SQLException;
}