package ru.shapedb.database.types;

public record ForeignKeyRelation(String otherTable, String otherColumn) { }
