package cz.maku.mommons.bukkit.hologram;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import lombok.Getter;

public abstract class AnimatedTextHologramLine extends TextHologramLine {

    @Getter
    private final double refreshRate;
    protected long count;

    public AnimatedTextHologramLine(HologramLine hdHologramLine, String text, double refreshRate) {
        super(hdHologramLine, text);
        this.refreshRate = refreshRate;
        this.count = 0;
    }

    public abstract void everyRefreshRate(long count);

}
