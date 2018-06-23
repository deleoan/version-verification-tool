package com.oocl.version.verifier.util;

import com.google.gson.Gson;
import com.oocl.version.verifier.model.EnvironmentsPOJO;

import javax.json.Json;
import javax.json.JsonReader;
import java.io.InputStream;

public class Util {

    private static final String PATH = "urls/environmentModules.json";

    public static EnvironmentsPOJO getEnvironmentUrlsObject() {
        ClassLoader classLoader = Util.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream(PATH);

        JsonReader reader = Json.createReader(is);
        String environmentModules = reader.readObject().toString();
        reader.close();
        return new Gson().fromJson(environmentModules, EnvironmentsPOJO.class);
    }
}
