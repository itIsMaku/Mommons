package cz.maku.mommons.bukkit.hologram;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import cz.maku.mommons.plugin.MommonsPlugin;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

@Data
public class Hologram {

    private final String id;
    private final List<HologramLine<?>> lines;
    @Getter(AccessLevel.PROTECTED)
    private final com.gmail.filoghost.holographicdisplays.api.Hologram hdHologram;
    private Location baseLocation;

    private <T> boolean canBeHologramLine(T t) {
        return t instanceof ItemStack || t instanceof String;
    }

    private String registerAnimated(AnimatedTextHologramLine animatedLine) {
        Collection<String> registeredPlaceholders = HologramsAPI.getRegisteredPlaceholders(MommonsPlugin.getPlugin());
        String textPlaceholder = String.format("{%s_animated_%s}", id, registeredPlaceholders.size());
        HologramsAPI.registerPlaceholder(MommonsPlugin.getPlugin(), textPlaceholder, animatedLine.getRefreshRate(), () -> {
            animatedLine.everyRefreshRate(animatedLine.count);
            animatedLine.count++;
            return animatedLine.get();
        });
        return textPlaceholder;
    }

    public void appendLine(HologramLine<?> line) {
        lines.add(line);
        if (line.get() instanceof ItemStack) {
            ItemStack stack = (ItemStack) line.get();
            hdHologram.appendItemLine(stack);
        }
        if (line.get() instanceof String) {
            String text = (String) line.get();
            if (line instanceof AnimatedTextHologramLine) {
                AnimatedTextHologramLine animatedLine = (AnimatedTextHologramLine) line;
                hdHologram.appendTextLine(registerAnimated(animatedLine));
            } else {
                hdHologram.appendTextLine(text);
            }
        }
        listeners();
    }

    public void insertLine(int row, HologramLine<?> line) {
        lines.add(line);
        if (line.get() instanceof ItemStack) {
            ItemStack stack = (ItemStack) line.get();
            hdHologram.insertItemLine(row, stack);
        }
        if (line.get() instanceof String) {
            String text = (String) line.get();
            if (line instanceof AnimatedTextHologramLine) {
                AnimatedTextHologramLine animatedLine = (AnimatedTextHologramLine) line;
                hdHologram.insertTextLine(row, registerAnimated(animatedLine));
            } else {
                hdHologram.insertTextLine(row, text);
            }
        }
        listeners();
    }

    public HologramLine<?> getLine(int index) {
        return lines.get(index);
    }

    public void removeLine(int index) {
        hdHologram.removeLine(index);
        lines.remove(index);
    }

    public void clearLines() {
        hdHologram.clearLines();
        lines.clear();
    }

    public int getSize() {
        return lines.size();
    }

    public void teleport(Location location) {
        hdHologram.teleport(location);
        baseLocation = location;
    }

    public void show(Player player) {
        hdHologram.getVisibilityManager().showTo(player);
    }

    public void hide(Player player) {
        hdHologram.getVisibilityManager().hideTo(player);
    }

    public void resetVisibility(Player player) {
        hdHologram.getVisibilityManager().resetVisibility(player);
    }

    public void resetVisibilityAll() {
        hdHologram.getVisibilityManager().resetVisibilityAll();
    }

    public boolean isVisible(Player player) {
        return hdHologram.getVisibilityManager().isVisibleTo(player);
    }

    public boolean isVisibleByDefault() {
        return hdHologram.getVisibilityManager().isVisibleByDefault();
    }

    public void setVisibleByDefault(boolean state) {
        hdHologram.getVisibilityManager().setVisibleByDefault(state);
    }

    public void delete() {
        hdHologram.delete();
        lines.clear();
        for (String placeholder : HologramsAPI.getRegisteredPlaceholders(MommonsPlugin.getPlugin())) {
            String checkedPlaceholder = placeholder.substring(1);
            if (checkedPlaceholder.startsWith(id)) {
                HologramsAPI.unregisterPlaceholder(MommonsPlugin.getPlugin(), placeholder);
            }
        }
    }

    private void listeners() {
        for (LinesChangeListener listener : Holograms.getListeners()) {
            listener.onChange(this);
        }
    }
}
