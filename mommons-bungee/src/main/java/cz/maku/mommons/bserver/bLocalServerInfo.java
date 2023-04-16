package cz.maku.mommons.bserver;

import lombok.Getter;

@Getter
public class bLocalServerInfo {

    private final String ip;
    private final int port;

    public bLocalServerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
