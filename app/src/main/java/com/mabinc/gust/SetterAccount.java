package com.mabinc.gust;

public class SetterAccount {

    public SetterAccount() {
    }

    private String key, name;
    private boolean isAvailable;

    public SetterAccount(String key, String name, boolean isAvailable) {
        this.key = key;
        this.name = name;
        this.isAvailable = isAvailable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
