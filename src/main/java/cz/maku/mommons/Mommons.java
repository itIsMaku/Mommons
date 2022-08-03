package cz.maku.mommons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mommons {

    public static final Gson GSON = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .create();

    public static final ExecutorService ES = Executors.newCachedThreadPool();
}
