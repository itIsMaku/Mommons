package cz.maku.mommons.worker;

import com.google.common.collect.Lists;
import cz.maku.mommons.worker.annotation.*;
import cz.maku.mommons.worker.annotation.sql.Download;
import cz.maku.mommons.worker.exception.ServiceNotFoundException;
import cz.maku.mommons.worker.type.ConsoleColors;
import cz.maku.mommons.worker.type.WorkerLoggerType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class WorkerMethod {

    private final Method method;
    private final Object object;

    public Object invoke(Object[] params) throws InvocationTargetException, IllegalAccessException {
        Parameter[] methodParameters = method.getParameters();
        if (params.length >= methodParameters.length) {
            return null;
        }
        Object[] o = new Object[methodParameters.length];
        for (int i = 0; i < params.length; i++) {
            if (!methodParameters[i].getType().isAssignableFrom(params[i].getClass())) {
                return null;
            }
            o[i] = params[i];
        }
        try {
            return method.invoke(object, o);
        } catch (Exception e) {
            WorkerLogger.blank(WorkerLoggerType.ERROR.getPrefix() + ConsoleColors.RESET + "Method " + method.getName() + " can't be invoked.");
            WorkerLogger.error(e);
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
                        throw new ServiceNotFoundException("Service " + parameterType.getName() + " can't be @Load-ed, because isn't registered.");
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
