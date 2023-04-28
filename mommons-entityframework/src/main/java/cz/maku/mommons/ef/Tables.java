package cz.maku.mommons.ef;

import com.google.common.collect.Lists;
import cz.maku.mommons.ef.annotation.AttributeName;
import cz.maku.mommons.ef.annotation.AutoIncrement;
import cz.maku.mommons.ef.annotation.Entity;
import cz.maku.mommons.ef.annotation.Id;
import cz.maku.mommons.ef.converter.TypeConverter;
import cz.maku.mommons.ef.entity.NamePolicy;
import cz.maku.mommons.ef.statement.MySQLStatementImpl;
import cz.maku.mommons.ef.statement.StatementType;
import cz.maku.mommons.utils.Reflections;
import cz.maku.mommons.utils.Texts;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;

public final class Tables {

    @Nullable
    public static String getSqlTableType(Class<?> clazz, int length) {
        if (clazz.equals(String.class)) {
            return "VARCHAR(" + length + ")";
        }
        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return "INT(" + length + ")";
        }
        if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return "BIGINT(" + length + ")";
        }
        if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return "INT(1)";
        }
        if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return "DOUBLE(" + length + ")";
        }
        if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return "FLOAT(" + length + ")";
        }
        if (clazz.equals(Short.class) || clazz.equals(short.class)) {
            return "SMALLINT(" + length + ")";
        }
        if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            return "TINYINT(" + length + ")";
        }
        return null;
    }

    @Nullable
    public static String getSqlTableType(Class<?> clazz) {
        if (clazz.equals(String.class)) {
            return getSqlTableType(clazz, 255);
        }
        if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return getSqlTableType(clazz, 1);
        }
        return getSqlTableType(clazz, 11);
    }

    public static <T> void createSqlTable(Connection connection, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        Constructor<?> constructor = Reflections.findConstructor(clazz, new Class<?>[]{});
        T instance = null;
        if (constructor != null) {
            instance = (T) constructor.newInstance();
        }
        String tableName = entityAnnotation.name();
        NamePolicy namePolicy = entityAnnotation.namePolicy();
        StringBuilder statement = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " ( ");
        String primaryKey = "";
        for (Field field : clazz.getDeclaredFields()) {
            if (!ColumnValidator.validateClass(field)) {
                throw new RuntimeException("Invalid class: " + field.getType().getName());
            }
            String addon;
            String columnName = field.getName();
            if (namePolicy.equals(NamePolicy.SQL)) {
                columnName = Texts.underscore(columnName);
            }
            if (field.isAnnotationPresent(AttributeName.class)) {
                columnName = field.getAnnotation(AttributeName.class).value();
            }
            boolean isIdColumn = field.isAnnotationPresent(Id.class);
            if (isIdColumn) {
                primaryKey = columnName;
            }
            String defaultValue = null;
            if (instance != null) {
                field.setAccessible(true);
                Object value = field.get(instance);
                if (value != null) {
                    if (value instanceof Boolean) {
                        value = ((boolean) value) ? 1 : 0;
                    }
                    defaultValue = "DEFAULT '" + value + "'";
                }
            } else if (!isIdColumn) {
                defaultValue = "DEFAULT NULL";
            }
            String autoIncrement = "";
            if (field.isAnnotationPresent(AutoIncrement.class)) {
                autoIncrement = "AUTO_INCREMENT";
                defaultValue = "NOT NULL";
            }
            if (defaultValue == null) {
                defaultValue = "NOT NULL";
            }
            String end = ", ";
            if (field.equals(clazz.getDeclaredFields()[clazz.getDeclaredFields().length - 1])) {
                end = ", PRIMARY KEY (`" + primaryKey + "`) );";
            }
            String sqlTableType = getSqlTableType(field.getType());
            if (sqlTableType == null) {
                TypeConverter<Object, Object> typeConverter = ColumnValidator.typeConverter(field);
                if (typeConverter == null) {
                    throw new RuntimeException("Invalid class: " + field.getType().getName());
                }
                for (Method method : typeConverter.getClass().getDeclaredMethods()) {
                    if (method.getName().equals("convertToColumn")) {
                        sqlTableType = getSqlTableType(method.getReturnType());
                        break;
                    }
                }
            }
            addon = String.format("`%s` %s %s %s%s", columnName, sqlTableType, defaultValue, autoIncrement, end);
            statement.append(addon);
        }
        System.out.println(statement.toString());
        MySQLStatementImpl mySQLStatement = new MySQLStatementImpl(statement.toString(), StatementType.CREATE, Collections.emptyMap());
        mySQLStatement.complete(connection);
    }
}
