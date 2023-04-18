package cz.maku.mommons.worker;

import com.google.common.collect.Maps;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.utils.ConsoleColors;
import cz.maku.mommons.utils.Texts;
import cz.maku.mommons.worker.annotation.Service;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Getter(AccessLevel.PROTECTED)
public class Worker {

    public final Map<Class<?>, WorkerServiceClass> workerClasses;
    private final Map<Class<?>, Object> specialServices;
    private final Map<Class<?>, Object> services;
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private Logger logger;
    private MySQL mySQL;

    public Worker() {
        specialServices = new HashMap<>();
        services = new HashMap<>();
        workerClasses = new HashMap<>();
        logger = Logger.getLogger("worker");
        logger.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                if (record.getLevel().equals(Level.INFO)) {
                    record.setLoggerName(ConsoleColors.GREEN_BRIGHT + Thread.currentThread().getName() + ConsoleColors.WHITE_BRIGHT);
                } else {
                    record.setLoggerName(Thread.currentThread().getName());
                }
                record.setMessage(Texts.getShortedClassName(record.getSourceClassName()) + " : " + record.getMessage());
            }

            @Override
            public void flush() {
                System.out.flush();
            }

            @Override
            public void close() throws SecurityException {
                System.out.close();
            }
        });
    }

    public void registerServices(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Service.class)) {
                services.put(clazz, null);
            } else {
                logger.severe("Registered service '" + clazz.getName() + "' is not annotated with @Service!");
            }
        }
    }

    public void setPublicMySQL(MySQL mySQL) {
        this.mySQL = mySQL;
    }

    public void registerSpecialServices(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Service.class)) {
                specialServices.put(clazz, null);
            } else {
                logger.severe("Registered special service '" + clazz.getName() + "' is not annotated with @Service!");
            }
        }
    }

    @SneakyThrows
    public void initialize() {
        initialize(specialServices);
        initialize(services);
    }

    public void stop() {
        for (Map.Entry<Class<?>, WorkerServiceClass> e : workerClasses.entrySet()) {
            e.getValue().destroy();
        }
        specialServices.clear();
        services.clear();
    }

    protected void initialize(Map<Class<?>, Object> services) throws Exception {
        for (Class<?> clazz : services.keySet()) {
            if (workerClasses.containsKey(clazz)) continue;
            AtomicReference<Object> service = new AtomicReference<>(null);
            if (services.get(clazz) != null) {
                service.set(services.get(clazz));
            } else {
                Arrays.stream(clazz.getDeclaredConstructors())
                        .sorted(Comparator.comparingInt(Constructor::getParameterCount))
                        .forEach(declaredConstructor -> {
                            if (service.get() == null) {
                                try {
                                    declaredConstructor.setAccessible(true);
                                    service.set(declaredConstructor.getParameterCount() == 0
                                            ? declaredConstructor.newInstance()
                                            : new WorkerExecutable(declaredConstructor, logger).invoke(this));
                                } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                                    logger.log(Level.SEVERE, e, e::getMessage);
                                }
                            }
                        });
                if (service.get() != null) {
                    services.put(clazz, service.get());
                } else {
                    logger.severe("Service '" + Texts.getShortedClassName(clazz) + "' has no suitable constructor!");
                }
            }
            initializeClass(clazz, service.get());
        }
    }

    protected void initializeClass(Class<?> clazz, Object service) {
        Map<String, WorkerExecutable> methods = Maps.newConcurrentMap();
        for (Method method : clazz.getDeclaredMethods()) {
            methods.put(method.getName(), new WorkerExecutable(method, service, logger));
        }
        Map<String, WorkerField> fields = Maps.newConcurrentMap();
        for (Field field : clazz.getDeclaredFields()) {
            fields.put(field.getName(), new WorkerField(service, field, null));
        }
        if (!make(clazz, service, methods, fields)) {
            WorkerServiceClass workerClass = new WorkerServiceClass(this, clazz.getAnnotation(Service.class), service, methods, fields, logger, Maps.newConcurrentMap());
            workerClass.initializeFields();
            workerClass.initializeMethods();
            workerClasses.put(clazz, workerClass);
            logger.info("Service '" + Texts.getShortedClassName(clazz) + "' was successfully initialized.");
        }
    }

    protected boolean make(Class<?> clazz, Object service, Map<String, WorkerExecutable> methods, Map<String, WorkerField> fields) {
        return false;
    }

}
