package cz.maku.mommons.bukkit.hologram;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import cz.maku.mommons.loader.MommonsLoader;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Holograms {

    @Getter
    private static final Map<String, Hologram> holograms = new ConcurrentHashMap<>();
    @Getter(AccessLevel.PROTECTED)
    private static final List<LinesChangeListener> listeners = Lists.newArrayList();

    public static Hologram create(String id, Lines lines, Location location) {
        com.gmail.filoghost.holographicdisplays.api.Hologram hdHologram = HologramsAPI.createHologram(MommonsLoader.getPlugin(), location);
        hdHologram.setAllowPlaceholders(true);
        Hologram hologram = new Hologram(id, Lists.newArrayList(), hdHologram);
        for (HologramLine<?> line : lines.get()) {
            hologram.appendLine(line);
        }
        holograms.put(id, hologram);
        return hologram;
    }

    public static void unregisterPlaceholders(Plugin plugin) {
        HologramsAPI.unregisterPlaceholders(plugin);
    }

    public static void registerListener(LinesChangeListener listener) {
        listeners.add(listener);
    }
}
