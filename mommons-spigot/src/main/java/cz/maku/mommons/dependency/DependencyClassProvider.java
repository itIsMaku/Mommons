package cz.maku.mommons.dependency;

import org.bukkit.plugin.Plugin;

public interface DependencyClassProvider {

   Class<? extends Dependency<?>> getDependencyClass();

   boolean canLoad(Plugin coreApplication);

}