package cz.maku.mommons.server;

import cz.maku.mommons.utils.Nets;
import cz.maku.mommons.worker.WorkerReceiver;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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

    public static CompletableFuture<LocalServerInfo> of() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return new LocalServerInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @SuppressWarnings("all")
    @NotNull
    public static LocalServerInfo local() {
        return WorkerReceiver.getCoreService(ServerDataService.class).getLocalServerInfo();
    }
}
