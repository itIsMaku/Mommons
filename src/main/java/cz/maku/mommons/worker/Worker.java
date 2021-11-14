package cz.maku.mommons.worker;

import com.google.common.collect.Maps;
import cz.maku.mommons.storage.cloud.CachedCloud;
import cz.maku.mommons.storage.cloud.PlayerCloud;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.*;
import cz.maku.mommons.worker.type.WorkerLoggerType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter(AccessLevel.PROTECTED)
public class Worker {

    private final Map<Class<?>, Object> specialServices;
    private final Map<Class<?>, Object> services;
    private final Map<Class<?>, WorkerServiceClass> workerClasses;
    private JavaPlugin javaPlugin;
    private MySQL mySQL;

    public Worker() {
        specialServices = new HashMap<>();
        services = new HashMap<>();
        workerClasses = new HashMap<>();
        Bukkit.getConsoleSender().sendMessage(WorkerLogger.format(WorkerLoggerType.INFO));
    }

    public <T> T getService(Class<T> tClass) {
        return (T) workerClasses.get(tClass).getObject();
    }

    public void registerServices(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Service.class)) {
                services.put(clazz, null);
                WorkerLogger.info("Service " + clazz.getName() + " was registered.");
            } else {
                WorkerLogger.error("Registered service " + clazz.getName() + " is not annotated with @Service.");
            }
        }
    }

    public void setPublicMySQL(MySQL mySQL) {
        this.mySQL = mySQL;
        WorkerLogger.info("Public MySQL was set.");
    }

    public void registerSpecialServices(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Service.class)) {
                specialServices.put(clazz, null);
                WorkerLogger.info("Special service " + clazz.getName() + " was registered.");
            } else {
                WorkerLogger.error("Registered service " + clazz.getName() + " is not annotated with @Service.");
            }
        }
    }

    public void setJavaPlugin(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        WorkerLogger.info("JavaPlugin set to class " + javaPlugin.getClass().getName() + ".");
    }

    @SneakyThrows
    public void initialize() {
        WorkerLogger.info("Initializing all special services...");
        initialize(specialServices);
        WorkerLogger.info("Initializing all services...");
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
            WorkerLogger.info("Service " + clazz.getName() + " was initialized.");
        }
        WorkerLogger.info("All services was initialized.");
    }

    protected void initializeClass(Class<?> clazz, Object service) {
        Map<String, WorkerMethod> methods = Maps.newConcurrentMap();
        for (Method method : clazz.getDeclaredMethods()) {
            methods.put(method.getName(), new WorkerMethod(method, service));
        }
        Map<String, WorkerField> fields = Maps.newConcurrentMap();
        for (Field field : clazz.getDeclaredFields()) {
            fields.put(field.getName(), new WorkerField(service, field, null));
        }
        WorkerServiceClass workerClass = new WorkerServiceClass(this, clazz.getAnnotation(Service.class), service, methods, fields, Maps.newConcurrentMap());
        workerClass.initializeFields();
        workerClass.initializeMethods();
        workerClasses.put(clazz, workerClass);
    }

}
