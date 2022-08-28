package cz.maku.mommons.bukkit.armorstand;

import cz.maku.mommons.bukkit.hologram.Hologram;
import cz.maku.mommons.bukkit.hologram.Holograms;
import cz.maku.mommons.bukkit.hologram.LinesChangeListener;
import cz.maku.mommons.worker.annotation.Initialize;
import cz.maku.mommons.worker.annotation.Service;

@Service(listener = true)
public class ArmorStandsBukkitService implements LinesChangeListener {

    @Initialize
    public void init() {
        Holograms.registerListener(this);
    }

    @Override
    public void onChange(Hologram hologram) {

    }
}
