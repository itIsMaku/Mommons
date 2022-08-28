package cz.maku.mommons.bukkit.hologram;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextHologramLine extends HologramLine<String> {

    private String text;

    public TextHologramLine(com.gmail.filoghost.holographicdisplays.api.line.HologramLine hdHologramLine, String text) {
        super(hdHologramLine);
        this.text = text;
    }

    @Override
    public String get() {
        return text;
    }
}
