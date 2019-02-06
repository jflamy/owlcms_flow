package org.ledocte.owlcms.data;

public enum AgeDivision {
    DEFAULT, SENIOR, JUNIOR, YOUTH, KIDS, MASTERS, TRADITIONAL, A, B, C, D;

    @Override
    public String toString() {
        return (isDefault() ? "" : name().charAt(0) + name().substring(1).toLowerCase());
    }

    public String getCode() {
        return (isDefault() ? "" : name().substring(0,1).toLowerCase());
    }

    public boolean isDefault() {
        return this == DEFAULT;
    }

    static public AgeDivision getAgeDivisionFromCode(String code) {
        for (AgeDivision curAD : AgeDivision.values()) {
            if (code.equals(curAD.getCode())) {
                return curAD;
            }
        }
        return AgeDivision.DEFAULT;
    }
}
