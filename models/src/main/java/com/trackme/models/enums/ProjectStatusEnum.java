package com.trackme.models.enums;

public enum ProjectStatusEnum {

    OPEN_PROJECT("OPEN_PROJECT"),
    CLOSED_PROJECT("CLOSED_PROJECT");

    private String name;

    ProjectStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ProjectStatusEnum findByName(String s){
        for(ProjectStatusEnum e : ProjectStatusEnum.values()){
            if(e.name.equals(s)){
                return e;
            }
        }
        return null;
    }
}
