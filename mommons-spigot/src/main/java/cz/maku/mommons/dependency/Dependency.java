package cz.maku.mommons.dependency;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public abstract class Dependency<T> {

    private final String name;
    private final Plugin plugin;
    private T object;

    public Dependency(String name, Plugin plugin) {
        this.name = name;
        this.plugin = plugin;
        T constructed = construct(plugin);
        if (constructed == null) {
            plugin.getLogger().warning("Dependency " + name + " could not be constructed.");
            return;
        }
        object = constructed;
    }

    @Nullable
    public abstract T construct(Plugin coreApplication);
    public abstract void init();

    public T reconstruct() {
        object = construct(plugin);
        return object;
    }

    public T get() {
        return object;
    }

    public String getName() {
        return name;
    }
}