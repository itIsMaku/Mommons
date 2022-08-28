package cz.maku.mommons.utils;

import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.util.List;

public final class Nets {

    public static boolean isAvailablePort(int port) {
        try {
            ServerSocket socket = new ServerSocket(port);
            socket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static int getAvailablePort(List<Integer> ports) {
        for (int port : ports) {
            if (!isAvailablePort(port)) continue;
            return port;
        }
        return -1;
    }

    public static String getAddress() throws IOException {
        URL url = new URL("https://api.ipify.org?format=json");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String json = bufferedReader.readLine().trim();
        return new JsonParser().parse(json).getAsJsonObject().get("ip").getAsString();
    }
}
