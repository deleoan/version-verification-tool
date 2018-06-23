package com.oocl.version.verifier.model;

import java.util.List;

public class Environment {
    private String envName;

    private List<Modules> Modules;

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public List<Modules> getModules() {
        return Modules;
    }

    public void setModules(List<Modules> Modules) {
        this.Modules = Modules;
    }

    @Override
    public String toString() {
        return "ClassPojo [envName = " + envName + ", Modules = " + Modules + "]";
    }
}