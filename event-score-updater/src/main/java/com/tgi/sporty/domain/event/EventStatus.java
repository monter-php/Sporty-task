package com.tgi.sporty.domain.event;

public enum EventStatus {
    LIVE("live"),
    NOT_LIVE("not_live");

    private final String value;

    EventStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
