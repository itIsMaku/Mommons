package cz.maku.mommons.storage.database;

import cz.maku.mommons.storage.database.type.MySQL;

import java.util.HashMap;
import java.util.Map;

public class SQLTable {

    private final String name;
    private final Map<String, Class<?>> columns;
    private final MySQL mySQL;

    public SQLTable(String name, MySQL mySQL) {
        this(name, mySQL, new HashMap<>());
    }

    public SQLTable(String name, MySQL mySQL, Map<String, Class<?>> columns) {
        this.name = name;
        this.columns = columns;
        this.mySQL = mySQL;
    }

    public String getName() {
        return name;
    }

    public Map<String, Class<?>> getColumns() {
        return columns;
    }

    public void addColumn(String key, Class<?> value) {
        columns.put(key, value);
    }

    public void create() {
        StringBuilder columnsString = null;
        for (String key : columns.keySet()) {
            Class<?> type = columns.get(key);
            String addon = key;
            String simpleName = type.getSimpleName().toLowerCase();
            switch (simpleName) {
                case "string" -> addon = key + " varchar(512)";
                case "idautoincrement" -> addon = key + " int NOT NULL primary key AUTO_INCREMENT";
                case "idstring" -> addon = key + " varchar(512) primary key";
                case "autoincrement" -> addon = key + " int NOT NULL AUTO_INCREMENT";
                case "timestamp" -> addon = key + " TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP";
                case "integer" -> addon = key + " int";
                case "json" -> addon = key + " JSON NULL";
            }
            if (columnsString == null) {
                columnsString = new StringBuilder("(" + addon);
            } else {
                columnsString.append(", ").append(addon);
            }
        }
        columnsString.append(")");
        mySQL.query(" ", "CREATE TABLE IF NOT EXISTS " + name + " " + columnsString + ";");
    }

}
