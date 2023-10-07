package cz.maku.mommons.ef;

import com.google.common.collect.Lists;
import com.google.gson.internal.Primitives;
import cz.maku.mommons.ef.annotation.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Tables {

    @Nullable
    public static String getSqlTableType(Class<?> clazz, int length) {
        clazz = Primitives.wrap(clazz);
        if (clazz.equals(String.class)) {
            return "VARCHAR(" + length + ")";
        }
        if (clazz.equals(Integer.class)) {
            return "INT(" + length + ")";
        }
        if (clazz.equals(Long.class)) {
            return "BIGINT(" + length + ")";
        }
        if (clazz.equals(Boolean.class)) {
            return "INT(1)";
        }
        if (clazz.equals(Double.class)) {
            return "DOUBLE(" + length + ")";
        }
        if (clazz.equals(Float.class)) {
            return "FLOAT(" + length + ")";
        }
        if (clazz.equals(Short.class)) {
            return "SMALLINT(" + length + ")";
        }
        if (clazz.equals(Byte.class)) {
            return "TINYINT(" + length + ")";
        }
        return null;
    }

    @Nullable
    public static String getSqlTableType(Class<?> clazz) {
        clazz = Primitives.wrap(clazz);
        if (clazz.equals(String.class)) {
            return getSqlTableType(clazz, 255);
        }
        if (clazz.equals(Boolean.class)) {
            return getSqlTableType(clazz, 1);
        }
        return getSqlTableType(clazz, 11);
    }

    public static <T> void createSqlTable(Connection connection, Class<T> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
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
        List<Field> declaredFields = Arrays.stream(clazz.getDeclaredFields()).filter(field -> !field.isAnnotationPresent(Ignored.class)).collect(Collectors.toList());
        for (Field field : declaredFields) {
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
            if (field.equals(declaredFields.get(declaredFields.size() - 1))) {
                end = ", PRIMARY KEY (`" + primaryKey + "`) );";
            }
            String sqlTableType = getSqlTableType(field.getType());
            if (sqlTableType == null) {
                TypeConverter<Object, Object> typeConverter = ColumnValidator.typeConverter(field);
                if (typeConverter == null) {
                    throw new RuntimeException("Invalid class: " + field.getType().getName());
                }
                for (Method method : Arrays.stream(typeConverter.getClass().getDeclaredMethods())
                        .filter(method -> !method.isBridge())
                        .collect(Collectors.toList())
                ) {
                    if (method.getName().equals("convertToColumn")) {
                        sqlTableType = getSqlTableType(method.getReturnType());
                        break;
                    }
                }
            }
            addon = String.format("`%s` %s %s %s%s", columnName, sqlTableType, defaultValue, autoIncrement, end);
            statement.append(addon);
        }
        MySQLStatementImpl mySQLStatement = new MySQLStatementImpl(statement.toString(), StatementType.CREATE, Collections.emptyMap());
        mySQLStatement.complete(connection);
    }
}
