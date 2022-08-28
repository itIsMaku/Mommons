package cz.maku.mommons.ef.entity;

import com.google.common.collect.Maps;
import cz.maku.mommons.ef.ColumnValidator;
import cz.maku.mommons.ef.annotation.AttributeName;
import cz.maku.mommons.ef.annotation.Entity;
import cz.maku.mommons.utils.Texts;
import lombok.AccessLevel;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Getter(AccessLevel.PROTECTED)
public class EntityClass {

    private final Class<?> clazz;
    private final String entityName;
    private final Field[] fields;

    private final Map<String, Field> columns;

    public EntityClass(Class<?> clazz, String entityName, Field[] fields, Map<String, Field> columns) {
        this.clazz = clazz;
        this.entityName = entityName;
        this.fields = fields;
        this.columns = columns;
    }

    public EntityClass(Class<?> clazz, String entityName, Field[] fields) {
        this.clazz = clazz;
        this.entityName = entityName;
        this.fields = fields;
        this.columns = resolveColumns();
    }

    protected Map<String, Field> resolveColumns() {
        Map<String, Field> columns = Maps.newHashMap();
        for (Field field : fields) {
            AtomicReference<String> column = new AtomicReference<>(field.getName());
            getChangedColumnAttributeName(field).ifPresent(column::set);
            if (getNamePolicy().equals(NamePolicy.SQL)) {
                column.set(Texts.underscore(column.get()));
            }
            if (!ColumnValidator.validateClass(field)) continue;
            columns.put(column.get(), field);
        }
        return columns;
    }

    public NamePolicy getNamePolicy() {
        return clazz.getAnnotation(Entity.class).namePolicy();
    }

    public Optional<String> getChangedColumnAttributeName(Field field) {
        if (!field.isAnnotationPresent(AttributeName.class)) return Optional.empty();
        return Optional.of(field.getAnnotation(AttributeName.class).value());
    }

}
