package com.oocl.version.verifier.model;

import java.util.List;

public class EnvironmentsPojo {
    private List<Environment> Environment;

    public List<Environment> getEnvironment() {
        return Environment;
    }

    @Override
    public String toString() {
        return "ClassPojo [Environment = " + Environment + "]";
    }
}