package cz.maku.mommons.server;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public final class ServerData {

    public static ImmutableMap<String, Server> getServers() {
        return ImmutableMap.copyOf(ServerDataRepository.SERVERS);
    }

    public static ImmutableMap<String, Server> getServersByCondition(BiFunction<String, Server, Boolean> function) {
        return ImmutableMap.copyOf(getServers().entrySet().stream().filter(e -> function.apply(e.getKey(), e.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    public static ImmutableMap<String, Server> getServersByType(@NotNull String type) {
        return getServersByCondition((id, server) -> type.equalsIgnoreCase(server.getType()));
    }

    @Nullable
    public static Server getServer(@NotNull String id) {
        return getServers().get(id);
    }
}
