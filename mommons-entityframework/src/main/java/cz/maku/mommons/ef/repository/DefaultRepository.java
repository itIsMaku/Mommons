package cz.maku.mommons.ef.repository;

import com.google.common.collect.Maps;
import cz.maku.mommons.Mommons;
import cz.maku.mommons.ef.ColumnValidator;
import cz.maku.mommons.ef.Entities;
import cz.maku.mommons.ef.RepositoryCache;
import cz.maku.mommons.ef.annotation.Id;
import cz.maku.mommons.ef.converter.TypeConverter;
import cz.maku.mommons.ef.statement.CompletedStatement;
import cz.maku.mommons.ef.statement.MySQLStatementImpl;
import cz.maku.mommons.ef.statement.StatementType;
import cz.maku.mommons.ef.statement.record.Record;
import cz.maku.mommons.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultRepository<ID, T> implements Repository<ID, T> {

    private final String table;
    private final Connection connection;
    private final String idColumn;
    private final Class<T> tClass;
    private final Class<ID> idClass;
    private final RepositoryCache<ID, T> repositoryCache;

    public DefaultRepository(String table, Connection connection, String idColumn, Class<T> tClass, Class<ID> idClass) {
        this.table = table;
        this.connection = connection;
        this.idColumn = idColumn;
        this.tClass = tClass;
        this.idClass = idClass;
        this.repositoryCache = new RepositoryCache<>(this);
    }

    private String prepareStatement(String query, Object... objects) {
        query = query.replace("{table}", table);
        query = query.replace("{id}", idColumn);
        return String.format(query, objects);
    }

    @Override
    public T select(ID id) {
        List<T> select = select(idColumn, id);
        if (select.isEmpty()) return null;
        return select.get(0);
    }

    @Override
    public List<T> selectAll() {
        MySQLStatementImpl mySQLStatement = new MySQLStatementImpl(prepareStatement("SELECT * FROM {table}"), StatementType.SELECT);
        return getObjectsFromStatement(mySQLStatement);
    }

    @Override
    public List<T> select(String key, Object value) {
        MySQLStatementImpl mySQLStatement = new MySQLStatementImpl(prepareStatement("SELECT * FROM {table} WHERE %s = ?", key), StatementType.SELECT);
        mySQLStatement.setArgumentValue(1, value);
        return getObjectsFromStatement(mySQLStatement);
    }

    @NotNull
    private List<T> getObjectsFromStatement(MySQLStatementImpl mySQLStatement) {
        CompletedStatement<MySQLStatementImpl> completedStatement = mySQLStatement.complete(connection);
        return completedStatement.getRecords().stream().map(record -> {
            try {
                return fromRecord(record);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    private Pair<String, Map<Integer, Object>> makeWhereCondition(Map<String, Object> conditions) {
        StringBuilder statement = new StringBuilder(" WHERE ");
        Map<Integer, Object> arguments = Maps.newHashMap();
        int i = 0;
        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            i++;
            String key = entry.getKey();
            Object value = entry.getValue();
            if (i != 1 || i < conditions.size()) {
                statement.append(" AND ");
            }
            statement.append(String.format("%s = ?", key));
            arguments.put(i, value);
        }
        return new Pair<>(statement.toString(), arguments);
    }

    @Override
    public List<T> selectMatching(T object) throws IllegalAccessException {
        Class<?> objectClass = object.getClass();
        StringBuilder statement = new StringBuilder(prepareStatement("SELECT * FROM {table}"));
        Map<String, Object> conditions = Maps.newHashMap();
        for (Field field : objectClass.getDeclaredFields()) {
            String column = Entities.getFieldName(objectClass, field);
            field.setAccessible(true);
            conditions.put(column, field.get(object));
        }
        Pair<String, Map<Integer, Object>> pair = makeWhereCondition(conditions);
        statement.append(pair.getFirst());
        MySQLStatementImpl mySQLStatement = new MySQLStatementImpl(statement.toString(), StatementType.SELECT, pair.getSecond());
        return getObjectsFromStatement(mySQLStatement);
    }

    @Override
    public List<T> selectFieldValues(Map<String, Object> values) {
        StringBuilder statement = new StringBuilder(prepareStatement("SELECT * FROM {table}"));
        Pair<String, Map<Integer, Object>> pair = makeWhereCondition(values);
        statement.append(pair.getFirst());
        MySQLStatementImpl mySQLStatement = new MySQLStatementImpl(statement.toString(), StatementType.SELECT, pair.getSecond());
        return getObjectsFromStatement(mySQLStatement);
    }

    @Override
    public void create(T object) throws IllegalAccessException {
        create(Collections.singleton(object));
    }

    @Override
    public void create(Collection<T> objects) throws IllegalAccessException {
        StringBuilder statement = new StringBuilder(prepareStatement("INSERT INTO {table} ("));
        StringBuilder valuesStatement = new StringBuilder(") VALUES ");
        Map<Integer, Object> arguments = Maps.newHashMap();
        int i = 0;
        int o = 0;
        for (T object : objects) {
            o++;
            valuesStatement.append("(");
            Class<?> objectClass = object.getClass();
            for (Field field : objectClass.getDeclaredFields()) {
                i++;
                String column = Entities.getFieldName(objectClass, field);
                statement.append(column);
                valuesStatement.append("?");
                if (i < objectClass.getDeclaredFields().length) {
                    statement.append(", ");
                    valuesStatement.append(", ");
                }
                field.setAccessible(true);
                arguments.put(i, field.get(object));
            }
            valuesStatement.append(")");
            if (o < objects.size()) {
                valuesStatement.append(", ");
            }
            statement.append(valuesStatement);

        }
        MySQLStatementImpl mySQLStatement = new MySQLStatementImpl(statement.toString(), StatementType.CREATE, arguments);
        System.out.println(Mommons.GSON.toJson(arguments));
        mySQLStatement.complete(connection);
    }

    @Override
    public T createIfNotExists(T object) throws IllegalAccessException {
        Class<?> objectClass = object.getClass();
        for (Field field : objectClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                T select = select((ID) field.get(object));
                if (select == null) {
                    create(object);
                    return object;
                }
                return select;
            }
        }
        return null;
    }

    @Override
    public boolean createOrUpdate(T object) throws IllegalAccessException {
        Class<?> objectClass = object.getClass();
        for (Field field : objectClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                T select = select((ID) field.get(object));
                if (select == null) {
                    create(object);
                    return false;
                }
                update(object);
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(T object) throws IllegalAccessException {
        Field idField = Arrays.stream(object.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Id.class)).findFirst().orElse(null);
        if (idField == null) {
            throw new RuntimeException("Entity " + object.getClass().getTypeName() + " has not @Id.");
        }
        idField.setAccessible(true);
        updateId(object, (ID) idField.get(object));
    }

    @Override
    public void updateId(T object, ID id) {
        Class<?> objectClass = object.getClass();
        StringBuilder statement = new StringBuilder(prepareStatement("UPDATE {table} SET "));
        int i = 0;
        for (Field field : objectClass.getDeclaredFields()) {
            i++;
            if (field.isAnnotationPresent(Id.class)) continue;
            String column = Entities.getFieldName(objectClass, field);
            statement.append(column).append(" = ?");
            if (i < objectClass.getDeclaredFields().length) {
                statement.append(", ");
            }
        }
        statement.append(prepareStatement(" WHERE {id} = ?"));
        MySQLStatementImpl mySQLStatement = new MySQLStatementImpl(statement.toString(), StatementType.UPDATE);
        int arg = 1;
        for (Field field : objectClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) continue;
            field.setAccessible(true);
            try {
                mySQLStatement.setArgumentValue(arg, field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            arg++;
        }
        mySQLStatement.setArgumentValue(arg, id);
        mySQLStatement.complete(connection);
    }

    @Override
    public void delete(T object) throws IllegalAccessException {
        Class<?> objectClass = object.getClass();
        Optional<Field> optional = Entities.findEntityIdField(objectClass);
        if (optional.isPresent()) {
            Field field = optional.get();
            field.setAccessible(true);
            deleteById((ID) field.get(object));
            return;
        }
        throw new RuntimeException("@Id field not found.");
    }

    @Override
    public void deleteById(ID id) {
        MySQLStatementImpl mySQLStatement = new MySQLStatementImpl(prepareStatement("DELETE FROM {table} WHERE {id} = ?"), StatementType.DELETE);
        mySQLStatement.setArgumentValue(1, id);
        mySQLStatement.complete(connection);
    }

    @Override
    public void delete(Collection<T> objects) throws IllegalAccessException {
        StringBuilder statement = new StringBuilder(prepareStatement("DELETE FROM {table} WHERE "));
        int i = 0;
        Map<Integer, Object> arguments = Maps.newHashMap();
        for (T object : objects) {
            Class<?> objectClass = object.getClass();
            Optional<Field> entityIdField = Entities.findEntityIdField(objectClass);
            if (entityIdField.isPresent()) {
                i++;
                Field field = entityIdField.get();
                statement.append("{id} = ? ");
                if (i < objects.size()) {
                    statement.append("OR ");
                }
                arguments.put(i, field.get(object));
            }

        }
        MySQLStatementImpl mySQLStatement = new MySQLStatementImpl(statement.toString(), StatementType.DELETE, arguments);
        mySQLStatement.complete(connection);
    }

    @Override
    public void deleteIds(Collection<ID> objects) {
        StringBuilder statement = new StringBuilder(prepareStatement("DELETE FROM {table} WHERE "));
        int i = 0;
        Map<Integer, Object> arguments = Maps.newHashMap();
        for (ID id : objects) {
            i++;
            statement.append("{id} = ? ");
            if (i < objects.size()) {
                statement.append("OR ");
            }
            arguments.put(i, id);
        }
        MySQLStatementImpl mySQLStatement = new MySQLStatementImpl(statement.toString(), StatementType.DELETE, arguments);
        mySQLStatement.complete(connection);
    }

    @Override
    public void deleteFieldValues(Map<String, Object> values) {

    }

    @Override
    public String toString(T object) {
        return null;
    }

    @Override
    public Class<T> getObjectClass() {
        return tClass;
    }

    @Override
    public Class<ID> getIdClass() {
        return idClass;
    }

    @Override
    public boolean isTableExists() {
        return false;
    }

    @Override
    public boolean idExists(ID id) {
        return false;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public String getTableName() {
        return table;
    }

    @Override
    public T createNew() {
        return null;
    }

    @Override
    public String idColumn() {
        return idColumn;
    }

    @Override
    public <R extends Record> T fromRecord(R record) throws InstantiationException, IllegalAccessException {
        Class<T> objectClass = getObjectClass();
        T object = objectClass.newInstance();
        for (Field field : object.getClass().getDeclaredFields()) {
            if (!ColumnValidator.validateClass(field)) {
                throw new RuntimeException("Class " + field.getType() + ", field " + field.getName() + " is not allowed. Use @AttributeConverter to field.");
            }
            String column = Entities.getFieldName(objectClass, field);
            Object value = record.getObject(column);
            if (!ColumnValidator.validateDefaultClasses(field)) {
                TypeConverter<Object, Object> typeConverter = ColumnValidator.typeConverter(field);
                if (typeConverter == null) {
                    throw new RuntimeException("TypeConverter for field " + field.getName() + " is null.");
                } else {
                    value = typeConverter.convertToEntityField(value);
                }
            }
            field.setAccessible(true);
            field.set(object, value);
        }
        return object;
    }

    @Override
    public boolean cache(ID id, T object) {
        return true;
    }

    @Override
    public boolean save(ID id, T object) {
        //updateId(object, id);
        return true;
    }

    @Override
    public RepositoryCache<ID, T> getRepositoryCache() {
        return repositoryCache;
    }

}
