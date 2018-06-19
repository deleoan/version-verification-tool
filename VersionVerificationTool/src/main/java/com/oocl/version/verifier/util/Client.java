package com.oocl.version.verifier.util;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class Client {

    public static final String NO_VERSION = "No Version";
    public static final String VERSION = "version";

    public static String getVersion(String domainUrl) throws InterruptedException, JSONException {
        AsyncHttpClient asyncHttpClient = asyncHttpClient();
        Future<Response> whenResponse = asyncHttpClient.prepareGet(domainUrl).execute();
        Response response;
        try {
            response = whenResponse.get();
            JSONObject jsonObj = new JSONObject(response.getResponseBody());
            return (String) jsonObj.get(VERSION);
        } catch (ExecutionException e) {
           return NO_VERSION;
        }
    }
}
