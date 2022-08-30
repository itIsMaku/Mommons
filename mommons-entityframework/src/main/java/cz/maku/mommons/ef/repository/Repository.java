package cz.maku.mommons.ef.repository;

import cz.maku.mommons.ef.statement.record.Record;
import cz.maku.mommons.storage.database.type.MySQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Repository<ID, T> {

    T select(ID id);

    List<T> selectAll();

    List<T> select(String key, Object value);

    List<T> selectMatching(T object) throws IllegalAccessException;

    List<T> selectFieldValues(Map<String, Object> values);

    void create(T object) throws IllegalAccessException;

    void create(Collection<T> object) throws IllegalAccessException;

    T createIfNotExists(T object) throws IllegalAccessException;

    boolean createOrUpdate(T object) throws IllegalAccessException;

    void update(T object) throws IllegalAccessException;

    void updateId(T object, ID id);

    void delete(T object) throws IllegalAccessException;

    void deleteById(ID id);

    void delete(Collection<T> objects) throws IllegalAccessException;

    void deleteIds(Collection<ID> objects);

    void deleteFieldValues(Map<String, Object> values);

    String toString(T object);

    Class<T> getObjectClass();

    Class<ID> getIdClass();

    boolean isTableExists();

    boolean idExists(ID id);

    Connection getConnection();

    String getTableName();

    T createNew();

    String idColumn();

    <R extends Record> T fromRecord(R record) throws InstantiationException, IllegalAccessException;

}
