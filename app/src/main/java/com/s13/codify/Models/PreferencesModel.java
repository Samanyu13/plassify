package com.s13.codify.Models;

public class PreferencesModel {
    private String label;
    private String info;

    public PreferencesModel(String label, String info) {
        this.label = label;
        this.info = info;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}