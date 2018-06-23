package com.oocl.version.verifier.model;

import java.util.List;

public class Modules {
    private String module;

    private List<String> links;

    public String getModule() {
        return module;
    }

    public List<String> getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return "ClassPojo [module = " + module + ", links = " + links + "]";
    }
}