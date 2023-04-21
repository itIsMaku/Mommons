package cz.maku.mommons.cloud;

import cz.maku.mommons.Response;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;

@ApiStatus.ScheduledForRemoval
@Deprecated
public interface CloudData {

    Object getCloudValue(String key);

    CompletableFuture<Response> setCloudValue(String key, Object value);

}
