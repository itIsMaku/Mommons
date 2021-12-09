package cz.maku.mommons.server;

import cz.maku.mommons.utils.Nets;
import cz.maku.mommons.worker.WorkerReceiver;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Getter
public class LocalServerInfo {

    private final String ip;
    private final int port;

    public LocalServerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public LocalServerInfo() throws IOException {
        this.ip = Nets.getAddress();
        this.port = Bukkit.getServer().getPort();
    }

    @SuppressWarnings("all")
    @NotNull
    public static LocalServerInfo local() {
        return WorkerReceiver.getCoreService(ServerDataService.class).getLocalServerInfo();
    }
}
