package cz.maku.mommons.bukkit.gui;

import com.google.common.collect.Maps;

import java.util.Map;

public abstract class Container {

    private final Map<Integer, Container> childContainers;

    public Container() {
        this.childContainers = Maps.newHashMap();
    }

    public void setContainer(int index, Container container) {
        childContainers.put(index, container);
    }

}
