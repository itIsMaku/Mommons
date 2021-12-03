package cz.maku.mommons.server;

import com.google.gson.JsonParser;
import cz.maku.mommons.worker.WorkerReceiver;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Getter
public class LocalServerInfo {

    private final String ip;
    private final int port;

    public LocalServerInfo() throws IOException {
        URL url = new URL("https://api.ipify.org?format=json");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String json = bufferedReader.readLine().trim();
        this.ip = new JsonParser().parse(json).getAsJsonObject().getAsString();
        this.port = Bukkit.getServer().getPort();
    }

    @SuppressWarnings("all")
    @NotNull
    public static LocalServerInfo local() {
        return WorkerReceiver.getCoreService(ServerDataService.class).getLocalServerInfo();
    }
}
