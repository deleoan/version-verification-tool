package com.oocl.version.verifier.util;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;

public class Util {
    public static JsonObject getEnvironmentUrlsObject(String path) {
        ClassLoader classLoader = Util.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream(path);

        JsonReader reader = Json.createReader(is);
        JsonObject empObj = reader.readObject();
        reader.close();
        return empObj;
    }
}
