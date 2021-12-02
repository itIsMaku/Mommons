package cz.maku.mommons.bukkit.hologram;

import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class ItemHologramLine extends HologramLine<ItemStack> {

    private final ItemStack stack;
    private final ItemLine itemLine;

    public ItemHologramLine(com.gmail.filoghost.holographicdisplays.api.line.HologramLine hdHologramLine, ItemStack stack, ItemLine itemLine) {
        super(hdHologramLine);
        this.stack = stack;
        this.itemLine = itemLine;
    }

    @Override
    public ItemStack get() {
        return stack;
    }
}
