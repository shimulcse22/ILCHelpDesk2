package com.shimul.ilchelpdesk;

public class CustomData<T> {
    private String id;
    private String name;

    public CustomData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}