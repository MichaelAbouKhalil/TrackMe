package com.trackme.models.enums;

public enum CriticalLevelEnum {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");

    private String name;

    CriticalLevelEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CriticalLevelEnum getByName(String name) {
        for (CriticalLevelEnum e : CriticalLevelEnum.values()) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }
}
