package cz.maku.mommons.utils;

import java.lang.reflect.Constructor;

public final class Reflections {

    public static Constructor<?> findConstructor(Class<?> clazz, Object[] params) {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] paramsTypes = constructor.getParameterTypes();
            if (paramsTypes.length == params.length) {
                boolean match = true;
                for (int i = 0; i < paramsTypes.length; ++i) {
                    if (!paramsTypes[i].isAssignableFrom(params[i].getClass())) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return constructor;
                }
            }
        }
        return null;
    }

}
