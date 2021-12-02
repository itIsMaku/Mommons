package cz.maku.mommons.bukkit.hologram;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class HologramLine<T> {

    private final com.gmail.filoghost.holographicdisplays.api.line.HologramLine hdHologramLine;

    public abstract T get();

}
