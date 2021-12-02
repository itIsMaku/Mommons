package cz.maku.mommons.bukkit.armorstand;

import cz.maku.mommons.bukkit.hologram.Hologram;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Data
public abstract class ArmorStand {

    private final String id;
    private final org.bukkit.entity.ArmorStand bukkitArmorStand;
    private final Hologram hologram;
    private final Location baseLocation;
    private ArmorStandContent content;

    public abstract void clickAction(Player player);

}
