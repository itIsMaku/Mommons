package cz.maku.mommons.dependency;

import com.google.common.collect.Maps;
import cz.maku.mommons.dependency.isolation.IsolatedEnvironment;
import cz.maku.mommons.dependency.isolation.IsolatedEnvironmentProvider;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public final class Dependencies {

    @SafeVarargs
    public static Map<Class<? extends DependencyClassProvider>, Dependency<?>> resolveDependencies(Plugin plugin, Class<? extends DependencyClassProvider>... classProviders) {
        Map<Class<? extends DependencyClassProvider>, Dependency<?>> dependencies = Maps.newHashMap();
        for (Class<? extends DependencyClassProvider> classProviderClass : classProviders) {
            DependencyClassProvider classProvider;
            try {
                classProvider = classProviderClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                plugin.getLogger().severe("Dependency: Error while creating provider instance of " + classProviderClass.getSimpleName() + "!");
                e.printStackTrace();
                continue;
            }
            Class<? extends Dependency<?>> dependencyClass = classProvider.getDependencyClass();
            boolean canLoad = classProvider.canLoad(plugin);
            if (!canLoad) {
                plugin.getLogger().warning("Dependency " + dependencyClass.getSimpleName() + " could not be loaded.");
                continue;
            }
            try {
                Dependency<?> dependency = dependencyClass.getConstructor(Plugin.class).newInstance(plugin);
                dependency.init();
                dependencies.put(classProviderClass, dependency);
            } catch (Exception e) {
                plugin.getLogger().warning("Dependency " + dependencyClass.getSimpleName() + " could not be loaded.");
                e.printStackTrace();
            }
        }
        return dependencies;
    }

    public static boolean canLoad(Plugin plugin, Class<? extends DependencyClassProvider> classProviderClass) {
        DependencyClassProvider classProvider;
        try {
            classProvider = classProviderClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            plugin.getLogger().severe("Dependency: Error while creating provider instance of " + classProviderClass.getSimpleName() + "!");
            e.printStackTrace();
            return false;
        }
        return classProvider.canLoad(plugin);
    }

    @Nullable
    public static <A, R> R solveIsolation(Plugin plugin, Class<? extends IsolatedEnvironmentProvider> clazz, A apply) {
        IsolatedEnvironmentProvider isolatedEnvironmentProvider;
        try {
            isolatedEnvironmentProvider = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            plugin.getLogger().severe("Dependency Isolation: Error while creating provider instance of " + clazz.getSimpleName() + "!");
            e.printStackTrace();
            return null;
        }
        Class<? extends IsolatedEnvironment<A, R>> environmentClass = (Class<? extends IsolatedEnvironment<A, R>>) isolatedEnvironmentProvider.getEnvironmentClass();
        if (!isolatedEnvironmentProvider.isRunnable(plugin)) {
            plugin.getLogger().warning("Dependency Isolation: Environment " + environmentClass.getSimpleName() + " is not runnable.");
            return null;
        }
        try {
            IsolatedEnvironment<A, R> isolatedEnvironment = environmentClass.getDeclaredConstructor().newInstance();
            return isolatedEnvironment.run(apply);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}