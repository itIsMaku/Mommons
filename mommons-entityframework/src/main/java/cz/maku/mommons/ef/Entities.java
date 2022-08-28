package cz.maku.mommons.ef;

import cz.maku.mommons.ef.annotation.Entity;
import cz.maku.mommons.ef.annotation.Id;
import cz.maku.mommons.ef.entity.EntityClass;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public final class Entities {

    @Nullable
    public static EntityClass entityClass(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            return null;
        }
        String entityName = clazz.getAnnotation(Entity.class).name();
        if (entityName.equals("none")) {
            entityName = clazz.getTypeName();
        }
        Field[] fields = clazz.getDeclaredFields();
        EntityClass entityClass = new EntityClass(clazz, entityName, fields);
        return entityClass;
    }

}
