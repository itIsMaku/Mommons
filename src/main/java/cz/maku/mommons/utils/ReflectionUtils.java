package cz.maku.mommons.utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public final class ReflectionUtils {

    public static Field getField(Class<?> clazz, String name) {
        Field field = null;
        while (clazz != null && field == null) {
            try {
                field = clazz.getDeclaredField(name);
            } catch (Exception ignored) {
            }
            clazz = clazz.getSuperclass();
        }
        return field;
    }

    public static void setField(@Nullable Object object, String fieldName, Object value) throws Exception {
        if (object == null) return;
        Field field = getField(object.getClass(), fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

}
