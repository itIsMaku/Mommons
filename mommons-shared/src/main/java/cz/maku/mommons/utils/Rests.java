package cz.maku.mommons.utils;

import com.google.common.collect.Maps;
import cz.maku.mommons.Mommons;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public final class Rests {

    private static <RES> Optional<RES> perform(Request request, Class<RES> clazz) throws IOException {
        Call call = Mommons.HTTP_CLIENT.newCall(request);
        Response response = call.execute();
        ResponseBody responseBody = response.body();
        if (responseBody == null) return Optional.empty();
        return Optional.of(Mommons.GSON.fromJson(responseBody.string(), clazz));
    }

    public static <RES> Optional<RES> get(String url, Class<RES> clazz, Map<String, String> headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .build();

        return perform(request, clazz);
    }

    public static <REQ, RES> Optional<RES> post(String url, REQ req, Class<RES> clazz, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(Mommons.GSON.toJson(req), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(body)
                .build();

        return perform(request, clazz);
    }

    public static <T> Optional<T> get(String url, Class<T> clazz) throws IOException {
        return get(url, clazz, Maps.newHashMap());
    }
}