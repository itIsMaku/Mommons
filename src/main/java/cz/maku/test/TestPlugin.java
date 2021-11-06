package cz.maku.test;

import cz.maku.mommons.worker.annotation.Plugin;
import cz.maku.mommons.worker.plugin.WorkerPlugin;

@Plugin(name = "Testovani", authors = {"itIsMaku"}, main = "cz.maku.test.TestPlugin", version = "1.1")
public class TestPlugin extends WorkerPlugin {

    @Override
    public void onLoad() {

    }

    @Override
    public void onUnload() {

    }

}
