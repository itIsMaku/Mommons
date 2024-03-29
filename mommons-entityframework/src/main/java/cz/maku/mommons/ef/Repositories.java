package cz.maku.mommons.ef;

import cz.maku.mommons.ef.annotation.AttributeName;
import cz.maku.mommons.ef.annotation.Entity;
import cz.maku.mommons.ef.annotation.Id;
import cz.maku.mommons.ef.entity.NamePolicy;
import cz.maku.mommons.ef.repository.DefaultRepository;
import cz.maku.mommons.ef.repository.Repository;
import cz.maku.mommons.utils.Texts;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import static cz.maku.mommons.utils.Reflections.findConstructor;

public final class Repositories {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <ID, T> Repository<ID, T> createRepository(Connection connection, Class<T> entityClass) throws InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        Entity entity = entityClass.getAnnotation(Entity.class);
        if (entity.repositoryClass() != null && entity.repositoryClass() != DefaultRepository.class) {
            Class<?> repositoryClass = entity.repositoryClass();
            Object[] arguments = new Object[]{connection, entityClass};
            Constructor<?> constructor = findConstructor(repositoryClass, arguments);
            if (constructor == null) {
                arguments = new Object[]{connection};
                constructor = findConstructor(repositoryClass, arguments);
                if (constructor == null) {
                    throw new SQLException("Not found public constructor.");
                }
            }
            return (Repository) constructor.newInstance(arguments);
        }
        String idColumn = "id";
        Class<ID> idClass = null;
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idClass = (Class<ID>) field.getType();
                idColumn = field.getName();
                if (field.isAnnotationPresent(AttributeName.class)) {
                    idColumn = field.getAnnotation(AttributeName.class).value();
                }
                if (entityClass.getAnnotation(Entity.class).namePolicy().equals(NamePolicy.SQL)) {
                    idColumn = Texts.underscore(idColumn);
                }
            }
        }
        return new DefaultRepository<>(entity.name(), connection, idColumn, entityClass, idClass);
    }

}
