package cz.maku.mommons.worker;

import com.google.common.collect.Lists;
import cz.maku.mommons.loader.MommonsLoader;
import cz.maku.mommons.worker.annotation.*;
import cz.maku.mommons.worker.annotation.sql.Download;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Getter
public class WorkerMethod {

    private final Method method;
    private final Object object;

    public Object invoke(Object[] params) throws InvocationTargetException, IllegalAccessException {
        Logger logger = MommonsLoader.getPlugin().getLogger();
        Parameter[] methodParameters = method.getParameters();
        if (params.length != methodParameters.length) {
            logger.severe("Method '" + method.getName() + "' can not be invoked. Parameters lengths are not same.");
            return null;
        }
        Object[] o = new Object[methodParameters.length];
        for (int i = 0; i < params.length; i++) {
            if (!methodParameters[i].getType().isAssignableFrom(params[i].getClass())) {
                logger.severe("Method '" + method.getName() + "' can not be invoked. Returning null.");
                return null;
            }
            o[i] = params[i];
        }
        try {
            return method.invoke(object, o);
        } catch (Exception e) {
            logger.severe("Method '" + method.getName() + "' can not be invoked.");
            e.printStackTrace();
            return null;
        }
    }


    @SneakyThrows
    public Object[] getLoadParameters(Worker worker) {
        List<Object> objects = Lists.newArrayList();
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Load.class)) {
                Class<?> parameterType = parameter.getType();
                if (worker.getServices().containsKey(parameterType)) {
                    if (worker.getServices().get(parameterType) == null) {
                        Object object = parameterType.newInstance();
                        worker.getServices().put(parameterType, object);
                        worker.initializeClass(parameterType, object);
                        objects.add(object);
                    }
                } else {
                    if (worker.getSpecialServices().containsKey(parameterType)) {
                        if (worker.getSpecialServices().get(parameterType) == null) {
                            Object object = parameterType.newInstance();
                            worker.getSpecialServices().put(parameterType, object);
                            worker.initializeClass(parameterType, object);
                            objects.add(object);
                        }
                    } else {
                        //logger.error("Cannot @Load class " + parameterType.getName() + ". Maybe is it Service?");
                        continue;
                    }
                }
            }
        }
        return objects.toArray(new Object[0]);
    }

    public boolean isRepeatTask() {
        return method.isAnnotationPresent(Repeat.class);
    }

    public boolean isAsync() {
        return method.isAnnotationPresent(Async.class);
    }

    public boolean isCommand() {
        return method.isAnnotationPresent(BukkitCommand.class);
    }

    public boolean isEvent() {
        return method.isAnnotationPresent(BukkitEvent.class);
    }

    public boolean isAnotherThread() {
        return method.isAnnotationPresent(AnotherThread.class);
    }

    public boolean isInit() {
        return method.isAnnotationPresent(Initialize.class);
    }

    public boolean isCondition() {
        return method.isAnnotationPresent(Condition.class);
    }

    public boolean isSqlDownload() {
        return method.isAnnotationPresent(Download.class);
    }

}
