package cz.maku.mommons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Mommons {

    public static final Gson GSON = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .create();

}
