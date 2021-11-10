package cz.maku.mommons.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import cz.maku.mommons.rest.annotation.Get;
import cz.maku.mommons.rest.annotation.Post;
import cz.maku.mommons.rest.annotation.RequestClass;
import cz.maku.mommons.rest.annotation.ResponseClass;
import okhttp3.*;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Optional;

public final class Rests {

    private static final Gson gson = new Gson();
    private static final OkHttpClient client = new OkHttpClient.Builder().build();

    // TODO: 10.11.2021 done this
    public static <RQ, RS> Optional<RS> send(String url, Class<? extends RestElement<RQ, RS>> restElement, @Nullable RQ body) throws RestException, IOException {
        if (!restElement.isAnnotationPresent(RequestClass.class) || !restElement.isAnnotationPresent(ResponseClass.class)) {
            throw new IllegalArgumentException("RestElement is not annotated with @RequestClass and @ResponseClass.");
        }
        Class<?> requestClass = restElement.getAnnotation(RequestClass.class).value();
        Class<?> responseClass = restElement.getAnnotation(ResponseClass.class).value();

        if (restElement.isAnnotationPresent(Get.class)) {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new RestException("ResponseBody from " + url + " is null.");
            }
            return (Optional<RS>) Optional.of(gson.fromJson(responseBody.string(), responseClass));
        } else if (restElement.isAnnotationPresent(Post.class)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(body));
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new RestException("ResponseBody from " + url + " is null.");
            }
            return (Optional<RS>) Optional.of(gson.fromJson(responseBody.string(), responseClass));
        }
        return null;
    }

}
