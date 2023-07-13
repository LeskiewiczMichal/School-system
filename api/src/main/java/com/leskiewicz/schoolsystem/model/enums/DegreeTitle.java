package com.leskiewicz.schoolsystem.model.enums;

public enum DegreeTitle {
    BACHELOR("Bachelor"),
    MASTER("Master"),
    DOCTOR("Doctor"),
    PROFESSOR("Professor");

    public final String label;

    DegreeTitle(String label) {
        this.label = label;
    }
}
