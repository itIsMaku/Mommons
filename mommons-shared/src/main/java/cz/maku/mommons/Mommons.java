package cz.maku.mommons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mommons {

    public static final Gson GSON = new GsonBuilder()
            .setLenient()
            .enableComplexMapKeySerialization()
            .create();

    public static final ExecutorService ES = Executors.newCachedThreadPool();

    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder()
            .build();
}
