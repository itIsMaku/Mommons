package cz.maku.mommons.worker;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.utils.ReflectionUtils;
import cz.maku.mommons.worker.annotation.*;
import cz.maku.mommons.worker.exception.ServiceNotFoundException;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Worker {

    private final Map<Class<?>, Object> specialServices = Maps.newConcurrentMap();
    private final Map<Class<?>, Object> services = Maps.newConcurrentMap();
    private JavaPlugin javaPlugin;

    public void registerServices(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Service.class)) {
                services.put(clazz, null);
            } else {
                throw new IllegalArgumentException("Registered service " + clazz.getName() + " is not annoted with @Service.");
            }
        }
    }

    public void registerSpecialServices(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Service.class)) {
                specialServices.put(clazz, null);
            } else {
                throw new IllegalArgumentException("Registered service " + clazz.getName() + " is not annoted with @Service.");
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

    private void initialize(Map<Class<?>, Object> services) throws Exception {
        for (Class<?> serviceClass : services.keySet()) {
            Object service;
            if (services.get(serviceClass) != null) {
                service = services.get(serviceClass);
            } else {
                service = serviceClass.newInstance();
                services.put(serviceClass, service);
            }
            initializeClazz(serviceClass, service);
        }
    }

    private void initializeClazz(Class<?> clazz, Object service) throws Exception {
        for (Method method : clazz.getDeclaredMethods()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Load.class)) {
                    Class<?> type = field.getType();
                    Object value = getServiceObject(type);
                    Object object = getServiceObject(clazz);
                    ReflectionUtils.setField(object, field.getName(), value);
                }
            }
            initializeMethod(clazz, service, method);
        }
    }

    private void initializeMethod(Class<?> clazz, Object service, Method method) throws Exception {
        Service serviceAnnotation = clazz.getAnnotation(Service.class);
        List<Object> parameters = Lists.newArrayList();
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Load.class)) {
                Class<?> parameterType = parameter.getType();
                if (services.containsKey(parameterType)) {
                    if (services.get(parameterType) == null) {
                        Object object = parameterType.newInstance();
                        services.put(parameterType, object);
                        initializeClazz(parameterType, object);
                        parameters.add(parameterType);
                    }
                } else {
                    if (specialServices.containsKey(parameterType)) {
                        if (specialServices.get(parameterType) == null) {
                            Object object = parameterType.newInstance();
                            specialServices.put(parameterType, object);
                            initializeClazz(parameterType, object);
                            parameters.add(parameterType);
                        }
                    } else {
                        throw new ServiceNotFoundException("Service " + parameterType.getName() + " can't be @Load-ed, because isn't registered.");
                    }
                }
            }
        }
        if (method.isAnnotationPresent(Initialize.class)) {
            if (method.isAnnotationPresent(AnotherThread.class)) {
                new Thread(() -> {
                    try {
                        invokeMethod(method, service, parameters.toArray());
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                invokeMethod(method, service, parameters.toArray());
            }
            return;
        }
        if (serviceAnnotation.scheduled() && method.isAnnotationPresent(Repeat.class)) {
            Repeat repeat = method.getAnnotation(Repeat.class);
            if (method.isAnnotationPresent(Async.class)) {
                boolean usedTaskParameter = false;
                for (Parameter parameter : method.getParameters()) {
                    if (parameter.getType().equals(BukkitTask.class)) {
                        usedTaskParameter = true;
                    }
                }
                boolean finalUsedTaskParameter = usedTaskParameter;
                Consumer<BukkitTask> consumer = task -> {
                    try {
                        if (finalUsedTaskParameter) {
                            parameters.add(task);
                        }
                        invokeMethod(method, service, parameters.toArray());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                };
                if (method.isAnnotationPresent(Async.class)) {
                    Bukkit.getScheduler().runTaskTimerAsynchronously(javaPlugin, consumer, repeat.delay(), repeat.period());
                } else {
                    Bukkit.getScheduler().runTaskTimer(javaPlugin, consumer, repeat.delay(), repeat.period());
                }
            }
            return;
        }
        if (serviceAnnotation.commands() && method.isAnnotationPresent(BukkitCommand.class)) {
            BukkitCommand command = method.getAnnotation(BukkitCommand.class);
            registerCommand(command.fallbackPrefix(), new Command(command.value(), command.description(), command.usage(), Arrays.asList(command.aliases())) {
                @SneakyThrows
                @Override
                public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                    CommandSender commandSender = null;
                    String[] commandArgs = null;
                    Type argsType = new TypeToken<String[]>() {
                    }.getType();
                    for (Parameter parameter : method.getParameters()) {
                        if (parameter.getParameterizedType().equals(argsType)) {
                            commandArgs = args;
                        }
                        if (parameter.getType().equals(CommandSender.class)) {
                            commandSender = sender;
                        }
                    }
                    if (commandSender == null) {
                        throw new IllegalArgumentException("CommandSender is required in BukkitCommand method.");
                    }
                    parameters.add(commandSender);
                    if (commandArgs != null) {
                        parameters.add(commandArgs);
                    }
                    invokeMethod(method, service, parameters.toArray());
                    return false;
                }
            });
            return;
        }
        if (serviceAnnotation.listener() && method.isAnnotationPresent(BukkitEvent.class)) {
            BukkitEvent listenerAnnotation = method.getAnnotation(BukkitEvent.class);
            Listener listener = new Listener() {};
            javaPlugin.getServer().getPluginManager().registerEvent(listenerAnnotation.value(), listener, listenerAnnotation.priority(), (l, e) -> {
                parameters.add(e);
                try {
                    invokeMethod(method, service, parameters.toArray());
                } catch (InvocationTargetException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }, javaPlugin, false);
            return;
        }
    }

    @SneakyThrows
    private void registerCommand(String fallbackPrefix, Command cmd) {
        Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        commandMap.register(fallbackPrefix, cmd);
    }

    protected boolean invokeMethod(Method method, @Nullable Object object, Object[] params) throws InvocationTargetException, IllegalAccessException {
        Parameter[] methodParameters = method.getParameters();
        if (params.length >= methodParameters.length) {
            return false;
        }
        Object[] o = new Object[methodParameters.length];
        for (int i = 0; i < params.length; i++) {
            if (!methodParameters[i].getType().isAssignableFrom(params[i].getClass())) {
                return false;
            }
            o[i] = params[i];
        }
        method.invoke(object, o);
        return true;
    }

    @Nullable
    private Object getServiceObject(Class<?> clazz) {
        if (services.containsKey(clazz)) return services.get(clazz);
        if (specialServices.containsKey(clazz)) return specialServices.get(clazz);
        return null;
    }

}
