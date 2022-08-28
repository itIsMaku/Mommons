package cz.maku.mommons.worker;

import com.google.common.collect.Maps;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.utils.Timers;
import cz.maku.mommons.worker.annotation.Repeat;
import cz.maku.mommons.worker.annotation.Service;
import cz.maku.mommons.worker.annotation.sql.Download;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Getter
public class WorkerServiceClass {

    private final Worker worker;
    private final Service service;
    private final Object object;
    private final Map<String, WorkerMethod> methods;
    private final Map<String, WorkerField> fields;
    private final Logger logger;
    private final Map<WorkerMethod, TimerTask> tasks;

    public WorkerServiceClass(Worker worker, Service service, Object object, Map<String, WorkerMethod> methods, Map<String, WorkerField> fields, Logger logger, Map<WorkerMethod, TimerTask> tasks) {
        this.worker = worker;
        this.service = service;
        this.object = object;
        this.methods = methods;
        this.fields = fields;
        this.logger = logger;
        this.tasks = tasks;
    }

    public WorkerServiceClass(Worker worker, Service service, Object object, Logger logger) {
        this(worker, service, object, Maps.newConcurrentMap(), Maps.newConcurrentMap(), logger, Maps.newConcurrentMap());
    }

    @SneakyThrows
    public void initializeFields() {
        for (WorkerField workerField : fields.values()) {
            Field field = workerField.getField();
            if (workerField.isLoad()) {
                Class<?> fieldType = field.getType();
                if (worker.getServices().containsKey(fieldType)) {
                    if (worker.getServices().get(fieldType) == null) {
                        Object object = fieldType.newInstance();
                        worker.getServices().put(fieldType, object);
                        worker.initializeClass(fieldType, object);
                        workerField.setValue(object);
                    } else {
                        workerField.setValue(worker.getServices().get(fieldType));
                    }
                } else if (worker.getSpecialServices().containsKey(fieldType)) {
                    if (worker.getSpecialServices().get(fieldType) == null) {
                        Object object = fieldType.newInstance();
                        worker.getSpecialServices().put(fieldType, object);
                        worker.initializeClass(fieldType, object);
                        workerField.setValue(object);
                    } else {
                        workerField.setValue(worker.getSpecialServices().get(fieldType));
                    }
                } else {
                    logger.severe("Cannot @Load class " + field.getName() + ". Maybe is it Service?");
                }
            }
        }
    }

    @SneakyThrows
    public void initializeMethods() {
        for (WorkerMethod workerMethod : methods.values()) {
            List<Object> params = new ArrayList<>(Arrays.asList(workerMethod.getLoadParameters(worker)));
            if (workerMethod.isInit()) {
                if (workerMethod.isAnotherThread()) {
                    new Thread(() -> {
                        try {
                            workerMethod.invoke(params.toArray());
                            params.clear();
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    workerMethod.invoke(params.toArray());
                    params.clear();
                }
                continue;
            }
            if (service.sql() && workerMethod.isSqlDownload()) {
                handleSqlDownload(workerMethod, params);
                continue;
            }
            if (service.scheduled() && workerMethod.isRepeatTask()) {
                handleRepeatTask(workerMethod, params);
                continue;
            }
            nextHandlers(workerMethod, params);
        }
    }

    protected void handleSqlDownload(WorkerMethod workerMethod, List<Object> params) {
        Download download = workerMethod.getMethod().getAnnotation(Download.class);
        Timers.repeat(task -> {
            try {
                workerMethod.invoke(new Object[]{MySQL.getApi().query(download.table(), download.query())});
                params.clear();
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }, download.delay(), download.period());
    }

    protected void handleRepeatTask(WorkerMethod workerMethod, List<Object> params) {
        Repeat repeat = workerMethod.getMethod().getAnnotation(Repeat.class);
        boolean usedTaskParameter = false;
        for (Parameter parameter : workerMethod.getMethod().getParameters()) {
            if (parameter.getType().equals(TimerTask.class)) {
                usedTaskParameter = true;
            }
        }
        boolean finalUsedTaskParameter = usedTaskParameter;
        Consumer<TimerTask> consumer = task -> {
            try {
                if (finalUsedTaskParameter) {
                    params.add(task);
                }
                workerMethod.invoke(params.toArray());
                params.clear();
                if (!tasks.containsKey(workerMethod)) {
                    tasks.put(workerMethod, task);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        };
        Timers.repeat(consumer, repeat.delay(), repeat.period());
    }

    protected void nextHandlers(WorkerMethod workerMethod, List<Object> params) {

    }

    @SneakyThrows
    public void destroy() {
        /*List<WorkerMethod> destroyers = methods.values().stream().filter(WorkerMethod::isDestroy).collect(Collectors.toList());
        for (WorkerMethod destroyer : destroyers) {
            Destroy destroy = destroyer.getMethod().getAnnotation(Destroy.class);
            long delay = destroy.delay();
            if (delay < 1) {
                if (destroyer.isAsync()) {
                    CompletableFuture.runAsync(() -> {
                        try {
                            destroyer.invoke(new Object[]{});
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    destroyer.invoke(new Object[]{});
                }
            } else {
                if (destroyer.isAsync()) {
                    Schedulers.laterBukkitAsync(task -> {
                        try {
                            destroyer.invoke(new Object[]{});
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }, delay);
                }
                Schedulers.later(task -> {
                    try {
                        destroyer.invoke(new Object[]{});
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }, delay);
            }
        }*/
    }

}
