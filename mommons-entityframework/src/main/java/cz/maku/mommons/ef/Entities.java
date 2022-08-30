package cz.maku.mommons.ef;

import cz.maku.mommons.ef.annotation.AttributeName;
import cz.maku.mommons.ef.annotation.Entity;
import cz.maku.mommons.ef.annotation.Id;
import cz.maku.mommons.ef.entity.EntityClass;
import cz.maku.mommons.ef.entity.NamePolicy;
import cz.maku.mommons.utils.Pair;
import cz.maku.mommons.utils.Texts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

public final class Entities {

    public static Optional<EntityClass> entityClass(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            return Optional.empty();
        }
        String entityName = clazz.getAnnotation(Entity.class).name();
        if (entityName.equals("none")) {
            entityName = clazz.getTypeName();
        }
        Field[] fields = clazz.getDeclaredFields();
        EntityClass entityClass = new EntityClass(clazz, entityName, fields);
        return Optional.of(entityClass);
    }

    public static Optional<NamePolicy> getEntityNamePolicy(Class<?> entityClass) {
        if (!entityClass.isAnnotationPresent(Entity.class)) return Optional.empty();
        return Optional.of(entityClass.getAnnotation(Entity.class).namePolicy());
    }

    @NotNull
    public static String getFieldName(Class<?> entityClass, Field field) {
        Optional<NamePolicy> optionalNamePolicy = getEntityNamePolicy(entityClass);
        String column = field.getName();
        if (optionalNamePolicy.isPresent()) {
            NamePolicy namePolicy = optionalNamePolicy.get();
            if (namePolicy.equals(NamePolicy.SQL)) {
                column = Texts.underscore(column);
            }
        }
        if (field.isAnnotationPresent(AttributeName.class)) {
            column = field.getAnnotation(AttributeName.class).value();
        }
        return column;
    }

    public static Optional<Field> findEntityIdField(Class<?> entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return Optional.of(field);
            }
        }
        return Optional.empty();
    }

}
