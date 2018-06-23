package com.oocl.version.verifier.util;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;

public class Util {

    public static final String PATH = "urls/environmentModules.json";

    public static JsonObject getEnvironmentUrlsObject() {
        ClassLoader classLoader = Util.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream(PATH);

        JsonReader reader = Json.createReader(is);
        JsonObject environmentUrlsObject = reader.readObject();
        reader.close();
        return environmentUrlsObject;
    }
}
