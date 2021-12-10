package cz.maku.mommons.worker;

import com.google.common.collect.Maps;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.Service;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Getter(AccessLevel.PROTECTED)
public class Worker {

    private final Map<Class<?>, Object> specialServices;
    private final Map<Class<?>, Object> services;
    private final Map<Class<?>, WorkerServiceClass> workerClasses;
    private JavaPlugin javaPlugin;
    private MySQL mySQL;
    private Logger logger;

    public Worker() {
        specialServices = new HashMap<>();
        services = new HashMap<>();
        workerClasses = new HashMap<>();
        logger = LoggerFactory.getLogger(Worker.class);
    }

    public <T> T getService(Class<T> tClass) {
        return (T) workerClasses.get(tClass).getObject();
    }

    public void registerServices(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Service.class)) {
                services.put(clazz, null);
            } else {
                logger.error("Registered service '" + clazz.getName() + "' is not annotated with @Service!");
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
                logger.error("Registered special service '" + clazz.getName() + "' is not annotated with @Service!");
            }
        }
    }

    public void setJavaPlugin(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    @SneakyThrows
    public void initialize() {
        initialize(specialServices);
        initialize(services);
    }

    public void stop() {
        specialServices.clear();
        services.clear();
        javaPlugin = null;
    }

    protected void initialize(Map<Class<?>, Object> services) throws Exception {
        for (Class<?> clazz : services.keySet()) {
            if (workerClasses.containsKey(clazz)) continue;
            Object service;
            if (services.get(clazz) != null) {
                service = services.get(clazz);
            } else {
                service = clazz.newInstance();
                services.put(clazz, service);
            }
            initializeClass(clazz, service);
        }
    }

    protected void initializeClass(Class<?> clazz, Object service) {
        Map<String, WorkerMethod> methods = Maps.newConcurrentMap();
        for (Method method : clazz.getDeclaredMethods()) {
            methods.put(method.getName(), new WorkerMethod(method, service, LoggerFactory.getLogger(clazz)));
        }
        Map<String, WorkerField> fields = Maps.newConcurrentMap();
        for (Field field : clazz.getDeclaredFields()) {
            fields.put(field.getName(), new WorkerField(service, field, null));
        }
        WorkerServiceClass workerClass = new WorkerServiceClass(this, clazz.getAnnotation(Service.class), service, methods, fields, Maps.newConcurrentMap());
        workerClass.initializeFields();
        workerClass.initializeMethods();
        workerClasses.put(clazz, workerClass);
        logger.info("Service '" + clazz.getName() + "' was successfully initialized.");
    }

}
