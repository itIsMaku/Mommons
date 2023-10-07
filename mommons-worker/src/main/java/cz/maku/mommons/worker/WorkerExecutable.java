package cz.maku.mommons.worker;

import com.google.common.collect.Lists;
import cz.maku.mommons.worker.annotation.*;
import cz.maku.mommons.worker.annotation.sql.Download;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Getter
public class WorkerExecutable {

    private final Executable executable;
    @Nullable
    private final Object object;
    private final Logger logger;

    public WorkerExecutable(Constructor<?> constructor, Logger logger) {
        this(constructor, null, logger);
    }

    public Object invoke(Worker worker) throws InvocationTargetException, IllegalAccessException {
        return invoke(getLoadParameters(worker));
    }

    public Object invoke(Object[] params) throws InvocationTargetException, IllegalAccessException {
        Parameter[] methodParameters = executable.getParameters();
        if (params.length != methodParameters.length) {
            logger.severe("Method '" + executable.getName() + "' can not be invoked. Parameters lengths are not same.");
            return null;
        }
        Object[] o = new Object[methodParameters.length];
        for (int i = 0; i < params.length; i++) {
            if (!methodParameters[i].getType().isAssignableFrom(params[i].getClass())) {
                logger.severe("Method '" + executable.getName() + "' can not be invoked. Returning null.");
                return null;
            }
            o[i] = params[i];
        }
        try {
            executable.setAccessible(true);
            if (executable instanceof Method) {
                return ((Method) executable).invoke(object, o);
            } else if (executable instanceof Constructor<?>) {
                try {
                    return ((Constructor<?>) executable).newInstance(o);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e, () -> "Constructor '" + executable.getName() + "' can not be invoked.");
                    return null;
                }
            } else {
                throw new RuntimeException("Unknown executable type: " + executable.getClass().getName());
            }
        } catch (Exception e) {
            logger.severe("Method '" + executable.getName() + "' can not be invoked.");
            e.printStackTrace();
            return null;
        }
    }


    @SneakyThrows
    public Object[] getLoadParameters(Worker worker) {
        List<Object> objects = Lists.newArrayList();
        for (Parameter parameter : executable.getParameters()) {
            if (parameter.isAnnotationPresent(Load.class)) {
                Class<?> parameterType = parameter.getType();
                Map<Class<?>, Object> services = worker.getServices();
                if (services.containsKey(parameterType) && services.get(parameterType) == null) {
                    Object object = parameterType.newInstance();
                    services.put(parameterType, object);
                    worker.initializeClass(parameterType, object);
                    objects.add(object);
                    continue;
                }
                logger.severe("Cannot @Load class " + parameterType.getName() + ". Maybe is it Service?");

            }
        }
        return objects.toArray(new Object[0]);
    }

    public boolean isRepeatTask() {
        return executable.isAnnotationPresent(Repeat.class);
    }

    public boolean isAsync() {
        return executable.isAnnotationPresent(Async.class);
    }

    public boolean isAnotherThread() {
        return executable.isAnnotationPresent(AnotherThread.class);
    }

    public boolean isInit() {
        return executable.isAnnotationPresent(Initialize.class);
    }

    public boolean isSqlDownload() {
        return executable.isAnnotationPresent(Download.class);
    }

    public boolean isDestroy() {
        return executable.isAnnotationPresent(Destroy.class);
    }

    public boolean isPostInit() {
        return executable.isAnnotationPresent(PostInitialize.class);
    }

}
