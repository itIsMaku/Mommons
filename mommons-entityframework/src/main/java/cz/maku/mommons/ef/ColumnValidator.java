package cz.maku.mommons.ef;

import com.google.common.collect.Lists;
import com.google.gson.internal.Primitives;
import cz.maku.mommons.ef.annotation.AttributeConvert;
import cz.maku.mommons.ef.converter.TypeConverter;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

public final class ColumnValidator {

    public static final List<Class<?>> DEFAULT_CLASSES = Lists.newArrayList(String.class, Integer.class, Double.class, Long.class, Float.class, Short.class, Byte.class, Boolean.class);

    public static boolean validateDefaultClasses(Field field) {
        return DEFAULT_CLASSES.contains(Primitives.wrap(field.getType()));
    }

    public static boolean validateClass(Field field) {
        if (DEFAULT_CLASSES.contains(Primitives.wrap(field.getType()))) return true;
        return typeConverter(field) != null;
    }

    @Nullable
    public static <X, Y> TypeConverter<X, Y> typeConverter(Field field) {
        if (!field.isAnnotationPresent(AttributeConvert.class)) return null;
        AttributeConvert attributeConvert = field.getAnnotation(AttributeConvert.class);
        Class<TypeConverter<X, Y>> converter = attributeConvert.converter();
        try {
            return converter.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
