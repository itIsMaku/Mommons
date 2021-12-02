package cz.maku.mommons.bukkit.armorstand;

import cz.maku.mommons.bukkit.hologram.Hologram;
import cz.maku.mommons.bukkit.hologram.Holograms;
import cz.maku.mommons.bukkit.hologram.Lines;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.EntityEquipment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ArmorStands {

    public static final double ARMOR_STAND_NAME_HEIGHT = 0.25;
    @Getter
    private static final Map<String, Hologram> holograms = new ConcurrentHashMap<>();

    public static ArmorStand create(String id, Lines lines, Location location, ArmorStandContent armorStandContent) {
        Location hologramLocation = location.clone().add(0, 2, 0);
        for (int i = 0; i < lines.get().size(); i++) {
            hologramLocation.add(0, ARMOR_STAND_NAME_HEIGHT, 0);
        }
        Hologram hologram = Holograms.create(id, lines, hologramLocation);
        org.bukkit.entity.ArmorStand bukkitArmorStand = location.getWorld().spawn(location, org.bukkit.entity.ArmorStand.class);
        EntityEquipment equipment = bukkitArmorStand.getEquipment();
        equipment.setHelmet(armorStandContent.getHead());
        equipment.setChestplate(armorStandContent.getChestplate());
        equipment.setLeggings(armorStandContent.getLeggings());
        equipment.setBoots(armorStandContent.getBoots());
        equipment.setItemInMainHand(armorStandContent.getLeftHand());
        equipment.setItemInOffHand(armorStandContent.getRightHand());
        return null;
    }

}
