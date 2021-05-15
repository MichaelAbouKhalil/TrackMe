package com.trackme.models.enums;

import java.util.Arrays;
import java.util.List;

public enum TicketStatusEnum {

    FIXED("FIXED", Arrays.asList()),
    TESTING("TESTING", Arrays.asList(TicketStatusEnum.FIXED)),
    IN_PROGRESS("IN_PROGRESS", Arrays.asList(TicketStatusEnum.TESTING)),
    VERIFIED("VERIFIED", Arrays.asList(TicketStatusEnum.IN_PROGRESS)),
    REJECTED("REJECTED", Arrays.asList()),
    NEW_TICKET("NEW_TICKET", Arrays.asList(VERIFIED, REJECTED));

    private String name;
    private List<TicketStatusEnum> nextStatus;

    TicketStatusEnum(String name, List<TicketStatusEnum> nextStatus) {
        this.name = name;
        this.nextStatus = nextStatus;
    }

    public String getName() {
        return name;
    }

    public List<TicketStatusEnum> getNextStatus() {
        return nextStatus;
    }

    public static TicketStatusEnum getStatusByName(String name) {
        for (TicketStatusEnum e : TicketStatusEnum.values()) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }

    public Boolean isNextStatusAccepted(TicketStatusEnum next){
        return nextStatus.contains(next);
    }

    public static Boolean isEndStatus(String status){
        TicketStatusEnum enumStatus = getStatusByName(status);
        return (TicketStatusEnum.REJECTED.equals(enumStatus))
                || (TicketStatusEnum.FIXED.equals(enumStatus));
    }
}
