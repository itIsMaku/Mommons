package cz.maku.mommons.worker;

import com.google.common.collect.Lists;
import cz.maku.mommons.worker.annotation.*;
import cz.maku.mommons.worker.annotation.sql.Download;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class WorkerMethod {

    private final Method method;
    private final Object object;
    private final Logger logger;

    public Object invoke(Object[] params) throws InvocationTargetException, IllegalAccessException {
        Parameter[] methodParameters = method.getParameters();
        if (params.length != methodParameters.length) {
            logger.severe("Method '" + method.getName() + "' can not be invoked. Parameters lengths are not same.");
            logger.severe("params Length: " + params.length);
            logger.severe("methodParameters: " + methodParameters.length);
            return null;
        }
        Object[] o = new Object[methodParameters.length];
        for (int i = 0; i < params.length; i++) {
            System.out.println(methodParameters[i].getType().getName());
            System.out.println(params[i].getClass().getName());
            if (!methodParameters[i].getType().isAssignableFrom(params[i].getClass())) {
                logger.severe("Method '" + method.getName() + "' can not be invoked. Returning null.");
                return null;
            }
            o[i] = params[i];
        }
        try {
            method.setAccessible(true);
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
                        logger.severe("Cannot @Load class " + parameterType.getName() + ". Maybe is it Service?");
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

    public boolean isDestroy() {
        return method.isAnnotationPresent(Destroy.class);
    }

}
