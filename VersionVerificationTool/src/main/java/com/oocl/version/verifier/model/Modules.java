package com.oocl.version.verifier.model;

import java.util.List;

public class Modules {
    private String module;

    private List<String> links;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "ClassPojo [module = " + module + ", links = " + links + "]";
    }
}