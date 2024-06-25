package cz.maku.mommons.dependency.isolation;

import cz.maku.mommons.dependency.Dependencies;
import cz.maku.mommons.dependency.DependencyClassProvider;
import org.bukkit.plugin.Plugin;

public abstract class IsolatedEnvironmentProvider {

    private final Class<? extends DependencyClassProvider> dependencyClassProvider;

    public IsolatedEnvironmentProvider(Class<? extends DependencyClassProvider> dependencyClassProvider) {
        this.dependencyClassProvider = dependencyClassProvider;
    }

    public abstract Class<? extends IsolatedEnvironment<?, ?>> getEnvironmentClass();

    public boolean isRunnable(Plugin plugin) {
        return Dependencies.canLoad(plugin, dependencyClassProvider);
    }
}