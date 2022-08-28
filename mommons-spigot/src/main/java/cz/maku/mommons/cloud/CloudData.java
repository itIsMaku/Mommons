package cz.maku.mommons.cloud;

import cz.maku.mommons.Response;

import java.util.concurrent.CompletableFuture;

public interface CloudData {

    Object getCloudValue(String key);

    CompletableFuture<Response> setCloudValue(String key, Object value);

}
